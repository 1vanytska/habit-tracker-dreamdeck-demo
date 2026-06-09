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
public class StepsSuggestionResponse {
    private List<String> steps;
    private boolean fromAi;
}
