package com.htdd.habittrackerdreamdeckdemo.dto;

import com.htdd.habittrackerdreamdeckdemo.model.Role;
import com.htdd.habittrackerdreamdeckdemo.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must be at most 255 characters")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 3, max = 30, message = "Password must be at least 3 characters")
    private String password;

    @NotNull
    private LocalDate registrationDate;

    @NotNull
    private String profilePicture;

    private Role role;

    private Status status;
}
