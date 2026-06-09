package com.htdd.habittrackerdreamdeckdemo.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/smart-goal-suggestion")
    public ResponseEntity<SmartGoalSuggestionResponse> getSmartGoalSuggestion(
            @RequestBody SmartGoalSuggestionRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(aiService.generateSmartSuggestion(request));
    }

    @PostMapping("/steps-suggestion")
    public ResponseEntity<StepsSuggestionResponse> getStepsSuggestion(
            @RequestBody StepsSuggestionRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(aiService.generateStepsSuggestion(request));
    }
}
