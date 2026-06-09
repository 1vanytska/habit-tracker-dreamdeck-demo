package com.htdd.habittrackerdreamdeckdemo.goal;

import com.htdd.habittrackerdreamdeckdemo.step.Step;
import com.htdd.habittrackerdreamdeckdemo.user.DefaultImagePaths;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;

    public List<Goal> getAllGoals() {
        return goalRepository.findAll();
    }

    public List<Goal> getGoalsByUserId(UUID userId) { return
            goalRepository.findByUserId(userId); }

    public Optional<Goal> getGoalById(UUID goalId) {
        return goalRepository.findById(goalId);
    }

    public Goal saveGoal(Goal goal) {
        if (goal.getPicture() == null || goal.getPicture().isBlank()) {
            UUID seed = goal.getGoalId() != null ? goal.getGoalId() : goal.getUserId();
            if (seed != null) {
                goal.setPicture(DefaultImagePaths.defaultGoalImageFor(seed));
            }
        }

        if (goal.getSteps() != null) {
            for (int i = 0; i < goal.getSteps().size(); i++) {
                Step step = goal.getSteps().get(i);
                step.setGoal(goal);
                step.setSortOrder(i);
            }
        }
        return goalRepository.save(goal);
    }

    public void deleteGoalById(UUID goalId) {
        goalRepository.deleteById(goalId);
    }
}
