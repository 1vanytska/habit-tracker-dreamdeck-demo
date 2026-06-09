package com.htdd.habittrackerdreamdeckdemo.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiControllerTest {

    @Mock
    private AiService aiService;

    @InjectMocks
    private AiController aiController;

    @Test
    void getSmartGoalSuggestion_ShouldReturnBadRequestWhenTitleMissing() {
        SmartGoalSuggestionRequest request = new SmartGoalSuggestionRequest();
        request.setDescription("Тестовий опис");

        var response = aiController.getSmartGoalSuggestion(request);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void getSmartGoalSuggestion_ShouldReturnOkWhenTitlePresent() {
        SmartGoalSuggestionRequest request = new SmartGoalSuggestionRequest();
        request.setTitle("Задеплойти проект");
        request.setDescription("Запустити проект на сервері");

        SmartGoalSuggestionResponse responseBody = SmartGoalSuggestionResponse.builder()
                .suggestedTitle("Досягти запуску проекту до 30 червня")
                .description("Оптимізувати деплой та перевірити продакшн")
                .steps(List.of("Налаштувати сервер для проекту", "Розгорнути проект і перевірити роботу"))
                .fromAi(false)
                .build();

        when(aiService.generateSmartSuggestion(any())).thenReturn(responseBody);

        var response = aiController.getSmartGoalSuggestion(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Досягти запуску проекту до 30 червня", response.getBody().getSuggestedTitle());
    }
}
