package com.banddreea.tracker.controller;

import com.banddreea.tracker.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam (required = false) String error,
                            @RequestParam (required = false) String logout,
                            @RequestParam(required = false) String registered,
                            Model model) {

        if (error != null) {
            model.addAttribute("error", "Invalid username or password.");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been signed out.");
        }

        if (registered != null) {
            model.addAttribute("message", "Account created. Please sign in.");
        }

        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {

        try {
            authService.register(username, email, password);
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
