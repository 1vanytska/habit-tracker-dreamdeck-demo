package com.htdd.habittrackerdreamdeckdemo.user;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private String username;
    private String description;
    private String profilePicture;
}