package com.vynohradov_nick.controller;



import com.vynohradov_nick.entity.Task;
import com.vynohradov_nick.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/tasks")
public class TaskController {



    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Список задач текущего пользователя
    @GetMapping
    public String listTasks(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("tasks", taskService.getTasksByUsername(username));
        return "task";
    }

    // Страница создания задачи
    @GetMapping("/create")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "create_task";
    }

    // Обработка создания задачи
    @PostMapping("/create")
    public String createTask(@ModelAttribute("task") Task task,
                             @RequestParam("files") MultipartFile[] files,
                             Authentication authentication,
                             Model model) {
        String username = authentication.getName();
        try {
            taskService.createTask(task.getTitle(), task.getDescription(), files, username);
        } catch (IOException ex) {
            model.addAttribute("error", "Error processing files");
            return "create_task";
        }
        return "redirect:/tasks";
    }

    // Детали задачи
    @GetMapping("/{id}")
    public String viewTask(@PathVariable Long id, Authentication authentication, Model model) {
        String username = authentication.getName();
        try {
            Task task = taskService.getTaskByIdAndUsername(id, username);
            model.addAttribute("task", task);
            return "task_detail";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "redirect:/tasks";
        }
    }

    // Страница редактирования задачи
    @GetMapping("/{id}/edit")
    public String showEditTaskForm(@PathVariable Long id, Authentication authentication, Model model) {
        String username = authentication.getName();
        try {
            Task task = taskService.getTaskByIdAndUsername(id, username);
            model.addAttribute("task", task);
            return "edit_task";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "redirect:/tasks";
        }
    }

    // Обработка редактирования задачи
    @PostMapping("/{id}/edit")
    public String updateTask(@PathVariable Long id,
                             @ModelAttribute("task") Task taskDetails,
                             Authentication authentication,
                             Model model) {
        String username = authentication.getName();
        try {
            taskService.updateTask(id, taskDetails, username);
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "edit_task";
        }
        return "redirect:/tasks";
    }

    // Удаление задачи
    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, Authentication authentication, Model model) {
        String username = authentication.getName();
        try {
            taskService.deleteTask(id, username);
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "redirect:/tasks";
    }
}