package com.htdd.habittrackerdreamdeckdemo.ai;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
class PollinationsRequest {
    private String model;
    private boolean json;
    private List<Map<String, String>> messages;
}
