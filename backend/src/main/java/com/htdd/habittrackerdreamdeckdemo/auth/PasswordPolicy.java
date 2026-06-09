package com.htdd.habittrackerdreamdeckdemo.auth;

public final class PasswordPolicy {

    private static final String SPECIAL_CHAR_PATTERN = ".*[!@#$%^&*(),.?\":{}|<>_\\-+=\\[\\]\\\\/;'~`].*";

    private PasswordPolicy() {
    }

    public static void validate(String password) {
        if (password == null || password.length() < 12) {
            throw new RuntimeException("Пароль має містити щонайменше 12 символів");
        }
        if (!password.matches(SPECIAL_CHAR_PATTERN)) {
            throw new RuntimeException("Пароль має містити хоча б один спеціальний символ");
        }
    }
}
