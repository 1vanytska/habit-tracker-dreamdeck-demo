package com.htdd.habittrackerdreamdeckdemo.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AiServiceTest {

  private final AiService aiService = new AiService(new ObjectMapper());

  @Test
  void generateSmartSuggestion_Fallback_ShouldReturnValidResponse() {
    SmartGoalSuggestionRequest request = new SmartGoalSuggestionRequest();
    request.setTitle("Вивчити Angular");
    request.setCategoryName("Навчання");
    request.setDeadline("2026-12-31");

    SmartGoalSuggestionResponse response = aiService.generateSmartSuggestion(request);

    assertNotNull(response.getDescription());
    assertFalse(response.getDescription().isBlank());
    assertNotNull(response.getSteps());
    assertFalse(response.getSteps().isEmpty());
  }

  @Test
  void generateStepsSuggestion_Fallback_ShouldReturnUseTitleInSteps() {
    StepsSuggestionRequest request = new StepsSuggestionRequest();
    request.setTitle("Задеплойти проект");
    request.setDescription("Запустити проект на продакшн");
    request.setCategoryName("Розробка");
    request.setDeadline("2026-07-01");

    StepsSuggestionResponse response = aiService.generateStepsSuggestion(request);

    assertNotNull(response);
    assertNotNull(response.getSteps());
    assertFalse(response.getSteps().isEmpty());
    assertTrue(response.getSteps().stream().anyMatch(step -> step.contains("проект") || step.contains("Задеплойти")));
  }
}
