package com.htdd.habittrackerdreamdeckdemo.step;

import com.htdd.habittrackerdreamdeckdemo.goal.Goal;
import com.htdd.habittrackerdreamdeckdemo.goal.GoalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StepServiceTest {

    @Mock
    private StepRepository stepRepository;

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private StepService stepService;

    @Test
    void createStepsBatch_ShouldCreateStepsWithOrder() {
        UUID goalId = UUID.randomUUID();
        Goal goal = new Goal();
        when(goalRepository.findById(goalId)).thenReturn(Optional.of(goal));
        when(stepRepository.countByGoal_GoalId(goalId)).thenReturn(1);
        when(stepRepository.save(any(Step.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Step> created = stepService.createStepsBatch(goalId, List.of("Крок 1", "Крок 2"));

        assertEquals(2, created.size());
        verify(stepRepository, times(2)).save(any(Step.class));
    }
}
