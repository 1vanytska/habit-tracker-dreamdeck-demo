package com.htdd.habittrackerdreamdeckdemo.goal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Goal>> getGoalsByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(goalService.getGoalsByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGoalById(@PathVariable UUID id) {
        return ResponseEntity.ok(goalService.getGoalById(id));
    }

    @PostMapping
//    @PreAuthorize("hasRole('USER')")
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
        return ResponseEntity.noContent().build();
    }
}
