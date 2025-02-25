package com.vynohradov_nick.service;

import com.vynohradov_nick.entity.Task;
import com.vynohradov_nick.entity.TaskAttachment;
import com.vynohradov_nick.entity.User;
import com.vynohradov_nick.repository.TaskAttachmentRepository;
import com.vynohradov_nick.repository.TaskRepository;
import com.vynohradov_nick.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class TaskService {


    private TaskRepository taskRepository;


    private TaskAttachmentRepository taskAttachmentRepository;


    private UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, TaskAttachmentRepository taskAttachmentRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskAttachmentRepository = taskAttachmentRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getTasksByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return taskRepository.findByUser(user);
    }

    public Task getTaskByIdAndUsername(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Task createTask(String title, String description, MultipartFile[] files, String username) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Используем конструктор Task(title, description, user)
        Task task = new Task(title, description, user);
        Task savedTask = taskRepository.save(task);
        if (files != null) {
            for (MultipartFile file : files) {
                TaskAttachment attachment = new TaskAttachment(file.getOriginalFilename(), file.getBytes(), savedTask);
                taskAttachmentRepository.save(attachment);
                savedTask.getAttachments().add(attachment);
            }
            taskRepository.save(savedTask);
        }
        return savedTask;
    }

    public Task updateTask(Long id, Task taskDetails, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }
}

