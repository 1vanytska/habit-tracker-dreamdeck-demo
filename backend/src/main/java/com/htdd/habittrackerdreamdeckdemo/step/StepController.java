package com.htdd.habittrackerdreamdeckdemo.step;

import com.htdd.habittrackerdreamdeckdemo.goal.Goal;
import com.htdd.habittrackerdreamdeckdemo.goal.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/steps")
@RequiredArgsConstructor
public class StepController {

    private final StepRepository stepRepository;
    private final GoalRepository goalRepository;
    private final StepService stepService;

    @PostMapping("/{goalId}")
    public ResponseEntity<Step> createStep(@PathVariable UUID goalId, @RequestBody Step stepRequest) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        int nextOrder = stepRepository.countByGoal_GoalId(goalId);
        Step step = new Step(stepRequest.getDescription(), goal);
        step.setSortOrder(nextOrder);
        return ResponseEntity.ok(stepRepository.save(step));
    }

    @PostMapping("/{goalId}/batch")
    public ResponseEntity<List<Step>> createStepsBatch(
            @PathVariable UUID goalId,
            @RequestBody List<String> descriptions) {
        return ResponseEntity.ok(stepService.createStepsBatch(goalId, descriptions));
    }

    @PutMapping("/goal/{goalId}/reorder")
    public ResponseEntity<List<Step>> reorderSteps(
            @PathVariable UUID goalId,
            @RequestBody List<UUID> orderedStepIds) {
        return ResponseEntity.ok(stepService.reorderSteps(goalId, orderedStepIds));
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
