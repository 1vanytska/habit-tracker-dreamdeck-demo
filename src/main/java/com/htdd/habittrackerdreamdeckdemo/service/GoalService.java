package com.htdd.habittrackerdreamdeckdemo.service;

import com.htdd.habittrackerdreamdeckdemo.model.Goal;
import com.htdd.habittrackerdreamdeckdemo.repository.GoalRepository;
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

    public List<Goal> getAllGoalsByUserId(UUID userId) { return
            goalRepository.findByUserId(userId); }

    public Optional<Goal> getGoalById(UUID goalId) {
        return goalRepository.findById(goalId);
    }

    public Goal saveGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    public void deleteGoalById(UUID goalId) {
        goalRepository.deleteById(goalId);
    }
}
