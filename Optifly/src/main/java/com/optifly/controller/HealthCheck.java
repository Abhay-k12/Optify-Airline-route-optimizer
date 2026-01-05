package com.optifly.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping("/api/test")
    public String test() {
        return "Spring Boot is running on port 8081!";
    }
}