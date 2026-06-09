package com.htdd.habittrackerdreamdeckdemo.goal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalService goalService;

    @Test
    void getGoalById_Positive_ShouldReturnGoal() {
        UUID goalId = UUID.randomUUID();
        Goal expectedGoal = new Goal();
        when(goalRepository.findById(goalId)).thenReturn(Optional.of(expectedGoal));

        Optional<Goal> result = goalService.getGoalById(goalId);

        assertTrue(result.isPresent());
        assertEquals(expectedGoal, result.get());
        verify(goalRepository, times(1)).findById(goalId);
    }

    @Test
    void getGoalById_Negative_ShouldReturnEmptyOptional() {
        UUID goalId = UUID.randomUUID();
        when(goalRepository.findById(goalId)).thenReturn(Optional.empty());

        Optional<Goal> result = goalService.getGoalById(goalId);

        assertFalse(result.isPresent());
        verify(goalRepository, times(1)).findById(goalId);
    }

    @Test
    void getGoalsByUserId_Positive_ShouldReturnGoalsList() {
        UUID userId = UUID.randomUUID();
        when(goalRepository.findByUserId(userId)).thenReturn(List.of(new Goal()));

        List<Goal> result = goalService.getGoalsByUserId(userId);

        assertEquals(1, result.size());
        verify(goalRepository, times(1)).findByUserId(userId);
    }

    @Test
    void saveGoal_Positive_ShouldReturnSavedGoal() {
        Goal goalToSave = new Goal();
        when(goalRepository.save(goalToSave)).thenReturn(goalToSave);

        Goal result = goalService.saveGoal(goalToSave);

        assertNotNull(result);
        verify(goalRepository, times(1)).save(goalToSave);
    }

    @Test
    void saveGoal_ShouldAssignDefaultPictureWhenMissing() {
        UUID userId = UUID.randomUUID();
        Goal goalToSave = new Goal();
        goalToSave.setUserId(userId);
        when(goalRepository.save(goalToSave)).thenReturn(goalToSave);

        Goal result = goalService.saveGoal(goalToSave);

        assertNotNull(result.getPicture());
        assertTrue(result.getPicture().contains("placeholder-goal"));
    }

    @Test
    void deleteGoalById_Positive_ShouldCallRepositoryDelete() {
        UUID goalId = UUID.randomUUID();

        goalService.deleteGoalById(goalId);

        verify(goalRepository, times(1)).deleteById(goalId);
    }
}