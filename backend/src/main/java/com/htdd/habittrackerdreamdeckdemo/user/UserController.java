package com.htdd.habittrackerdreamdeckdemo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UserProfileUpdateRequest request) {

        User updatedUser = userService.updateUserProfile(user, request);
        return ResponseEntity.ok(updatedUser);
    }
}