package com.htdd.habittrackerdreamdeckdemo.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmartGoalSuggestionResponse {
    private String suggestedTitle;
    private String description;
    private List<String> steps;
    private boolean fromAi;
}
