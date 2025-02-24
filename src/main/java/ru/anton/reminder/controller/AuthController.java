package ru.anton.reminder.controller;

import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class AuthController {
    @GetMapping
    public Map<String, Object> getUser(@AuthenticationPrincipal OAuth2User user) {
        return user != null ? user.getAttributes() : Map.of("error", "Not authenticated");
    }
}
