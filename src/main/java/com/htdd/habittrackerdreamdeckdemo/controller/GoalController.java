package com.htdd.habittrackerdreamdeckdemo.controller;

import com.htdd.habittrackerdreamdeckdemo.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
