package com.htdd.habittrackerdreamdeckdemo.ai;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StepsSuggestionRequest {
    private String title;
    private String categoryName;
    private String description;
    private String startDate;
    private String deadline;
    private List<String> existingSteps = new ArrayList<>();
}
