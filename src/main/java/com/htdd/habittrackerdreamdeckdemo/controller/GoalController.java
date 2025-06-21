package com.htdd.habittrackerdreamdeckdemo.controller;

import com.htdd.habittrackerdreamdeckdemo.model.Goal;
import com.htdd.habittrackerdreamdeckdemo.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @GetMapping
    public ResponseEntity<?> getAllGoal() {
        return ResponseEntity.ok(goalService.getAllGoals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGoalById(@PathVariable UUID id) {
        return ResponseEntity.ok(goalService.getGoalById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveGoal(@Valid @RequestBody Goal goal) {
        return ResponseEntity.ok(goalService.saveGoal(goal));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateGoal(@PathVariable UUID id, @Valid @RequestBody Goal goal) {
        return ResponseEntity.ok(goalService.saveGoal(goal));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteGoal(@PathVariable UUID id) {
        goalService.deleteGoalById(id);
//        return ResponseEntity.ok("Goal deleted successfully");
        return ResponseEntity.noContent().build();
    }
}
