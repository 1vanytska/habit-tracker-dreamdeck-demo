package com.htdd.habittrackerdreamdeckdemo.user;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultImagePathsTest {

    @Test
    void defaultAvatarFor_ShouldReturnAssetPath() {
        String path = DefaultImagePaths.defaultAvatarFor(UUID.randomUUID());

        assertNotNull(path);
        assertTrue(path.contains("user.svg"));
    }

    @Test
    void defaultGoalImageFor_ShouldReturnAssetPath() {
        String path = DefaultImagePaths.defaultGoalImageFor(UUID.randomUUID());

        assertNotNull(path);
        assertTrue(path.contains("placeholder-goal"));
    }
}
