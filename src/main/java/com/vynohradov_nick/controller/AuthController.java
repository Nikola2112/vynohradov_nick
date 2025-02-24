package com.vynohradov_nick.controller;



import com.vynohradov_nick.entity.User;
import com.vynohradov_nick.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {


    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Страница регистрации
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Обработка регистрации
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user, BindingResult result, Model model) {
        try {
            userService.registerUser(user);
        } catch (RuntimeException ex) {
            result.rejectValue("username", "error.user", ex.getMessage());
            return "register";
        }
        return "redirect:/login?registered";
    }

    // Страница логина (обработку формы осуществляет Spring Security)
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}