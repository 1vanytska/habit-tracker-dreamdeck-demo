package com.htdd.habittrackerdreamdeckdemo.ai;

import lombok.Data;

@Data
public class SmartGoalSuggestionRequest {
    private String title;
    private String categoryName;
    private String description;
    private String startDate;
    private String deadline;
}
