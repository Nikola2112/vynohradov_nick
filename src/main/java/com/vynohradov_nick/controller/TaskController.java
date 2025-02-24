package com.vynohradov_nick.controller;





import com.vynohradov_nick.entity.Task;
import com.vynohradov_nick.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {


    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Получить список задач для аутентифицированного пользователя
    @GetMapping
    public List<Task> getTasks(Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User)
                authentication.getPrincipal()).getUsername();
        return taskService.getTasksByUsername(username);
    }

    // Получить задачу по id для аутентифицированного пользователя
    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id, Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User)
                authentication.getPrincipal()).getUsername();
        try {
            Task task = taskService.getTaskByIdAndUsername(id, username);
            return ResponseEntity.ok(task);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Создать новую задачу с опциональными прикреплёнными файлами
    @PostMapping
    public ResponseEntity<?> createTask(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User)
                authentication.getPrincipal()).getUsername();
        try {
            Task task = taskService.createTask(title, description, files, username);
            return ResponseEntity.ok(task);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing files");
        }
    }

    // Обновить задачу
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id,
                                        @RequestBody Task taskDetails,
                                        Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User)
                authentication.getPrincipal()).getUsername();
        try {
            Task updatedTask = taskService.updateTask(id, taskDetails, username);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Удалить задачу
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User)
                authentication.getPrincipal()).getUsername();
        try {
            taskService.deleteTask(id, username);
            return ResponseEntity.ok("Task deleted");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}