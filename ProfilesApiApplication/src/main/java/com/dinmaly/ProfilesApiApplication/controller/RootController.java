package com.dinmaly.ProfilesApiApplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
@RestController
public class RootController {
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> rootCheck() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "Profiles API Root is live. Use /api/profiles for data.");
        return ResponseEntity.ok(response);
    }
}
