package com.htdd.habittrackerdreamdeckdemo.step;

import com.htdd.habittrackerdreamdeckdemo.goal.Goal;
import com.htdd.habittrackerdreamdeckdemo.goal.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/steps")
@RequiredArgsConstructor
public class StepController {

    private final StepRepository stepRepository;
    private final GoalRepository goalRepository;

    @PostMapping("/{goalId}")
    public ResponseEntity<Step> createStep(@PathVariable UUID goalId, @RequestBody Step stepRequest) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        Step step = new Step(stepRequest.getDescription(), goal);
        return ResponseEntity.ok(stepRepository.save(step));
    }

    @PutMapping("/{stepId}/toggle")
    public ResponseEntity<Step> toggleStep(@PathVariable UUID stepId) {
        Step step = stepRepository.findById(stepId)
                .orElseThrow(() -> new RuntimeException("Step not found"));

        step.setCompleted(!step.isCompleted());
        return ResponseEntity.ok(stepRepository.save(step));
    }

    @DeleteMapping("/{stepId}")
    public ResponseEntity<?> deleteStep(@PathVariable UUID stepId) {
        stepRepository.deleteById(stepId);
        return ResponseEntity.noContent().build();
    }
}