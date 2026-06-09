package com.htdd.habittrackerdreamdeckdemo.user;

import java.util.UUID;

public final class DefaultImagePaths {

    private static final String USER_AVATAR = "/assets/images/user.svg";
    private static final String GOAL_IMAGE = "/assets/images/placeholder-goal.png";

    private DefaultImagePaths() {
    }

    public static String defaultAvatarFor(UUID userId) {
        return USER_AVATAR;
    }

    public static String defaultGoalImageFor(UUID seed) {
        return GOAL_IMAGE;
    }
}
