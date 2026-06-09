package com.htdd.habittrackerdreamdeckdemo.step;

import com.htdd.habittrackerdreamdeckdemo.goal.Goal;
import com.htdd.habittrackerdreamdeckdemo.goal.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StepService {

    private final StepRepository stepRepository;
    private final GoalRepository goalRepository;

    public List<Step> reorderSteps(UUID goalId, List<UUID> orderedStepIds) {
        goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Ціль не знайдена"));

        List<Step> steps = stepRepository.findByGoal_GoalIdOrderBySortOrderAsc(goalId);
        Map<UUID, Step> stepMap = steps.stream()
                .collect(Collectors.toMap(Step::getStepId, step -> step));

        if (orderedStepIds.size() != steps.size() || !stepMap.keySet().containsAll(orderedStepIds)) {
            throw new RuntimeException("Невірний порядок кроків");
        }

        for (int i = 0; i < orderedStepIds.size(); i++) {
            stepMap.get(orderedStepIds.get(i)).setSortOrder(i);
        }

        stepRepository.saveAll(steps);
        return stepRepository.findByGoal_GoalIdOrderBySortOrderAsc(goalId);
    }

    public List<Step> createStepsBatch(UUID goalId, List<String> descriptions) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Ціль не знайдена"));

        int startOrder = stepRepository.countByGoal_GoalId(goalId);
        List<Step> created = new ArrayList<>();

        for (int i = 0; i < descriptions.size(); i++) {
            String description = descriptions.get(i);
            if (description == null || description.isBlank()) {
                continue;
            }
            Step step = new Step(description.trim(), goal);
            step.setSortOrder(startOrder + created.size());
            created.add(stepRepository.save(step));
        }

        return created;
    }
}
