package com.htdd.habittrackerdreamdeckdemo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me/description")
    public ResponseEntity<?> updateDescription(@AuthenticationPrincipal User user, @RequestBody String newDescription) {
        user.setDescription(newDescription);
        userRepository.save(user);
        return ResponseEntity.ok("Description updated");
    }
}