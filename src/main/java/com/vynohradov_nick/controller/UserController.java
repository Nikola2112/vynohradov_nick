package com.vynohradov_nick.controller;


import com.vynohradov_nick.entity.User;
import com.vynohradov_nick.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User)
                authentication.getPrincipal()).getUsername();
        try {
            User user = userService.findByUsername(username);
            // Не возвращаем пароль в ответе – в продакшене используйте DTO
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}