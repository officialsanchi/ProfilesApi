package com.dinmaly.ProfilesApiApplication.controller;

import com.dinmaly.ProfilesApiApplication.dto.ProfileDTOs;
import com.dinmaly.ProfilesApiApplication.model.Profile;
import com.dinmaly.ProfilesApiApplication.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "*")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProfile(@RequestBody(required = false) Map<String, Object> body) {

        if (body == null || !body.containsKey("name")) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Missing required field: name");
        }

        Object nameObj = body.get("name");

        if (!(nameObj instanceof String)) {
            return errorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid type for field: name");
        }

        String name = ((String) nameObj).trim();
        if (name.isEmpty()) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Name cannot be empty");
        }

        ProfileService.CreateResult result = profileService.createProfile(name);
        ProfileDTOs.ProfileResponse profileResponse = profileService.toFullResponse(result.profile());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        if (result.alreadyExisted()) {
            response.put("message", "Profile already exists");
        }
        response.put("data", profileResponse);

        HttpStatus status = result.alreadyExisted() ? HttpStatus.OK : HttpStatus.CREATED;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable String id) {
        Profile profile = profileService.getById(id);
        ProfileDTOs.ProfileResponse profileResponse = profileService.toFullResponse(profile);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", profileResponse);

        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProfiles(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String country_id,
            @RequestParam(required = false) String age_group) {

        List<Profile> profiles = profileService.getAll(gender, country_id, age_group);
        List<ProfileDTOs.ProfileSummary> summaries = profiles.stream()
                .map(profileService::toSummary)
                .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("count", summaries.size());
        response.put("data", summaries);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String id) {
        profileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String, Object>> errorResponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "error");
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
