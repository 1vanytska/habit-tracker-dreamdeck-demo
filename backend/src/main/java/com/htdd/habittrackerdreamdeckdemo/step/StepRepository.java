package com.htdd.habittrackerdreamdeckdemo.step;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StepRepository extends JpaRepository<Step, UUID> {

    int countByGoal_GoalId(UUID goalId);

    List<Step> findByGoal_GoalIdOrderBySortOrderAsc(UUID goalId);
}