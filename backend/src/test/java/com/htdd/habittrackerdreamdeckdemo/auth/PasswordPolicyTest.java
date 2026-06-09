package com.htdd.habittrackerdreamdeckdemo.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordPolicyTest {

    @Test
    void validate_Positive_ShouldAcceptStrongPassword() {
        assertDoesNotThrow(() -> PasswordPolicy.validate("validpassword!"));
    }

    @Test
    void validate_Negative_ShouldRejectShortPassword() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> PasswordPolicy.validate("short1!"));

        assert exception.getMessage().contains("12 символів");
    }

    @Test
    void validate_Negative_ShouldRejectWithoutSpecialCharacter() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> PasswordPolicy.validate("verylongpassword"));

        assert exception.getMessage().contains("спеціальний символ");
    }
}
