package com.htdd.habittrackerdreamdeckdemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    public User(String email, String username, String password,
                LocalDate registrationDate, String profilePicture,
                Role role, Status status) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.registrationDate = registrationDate;
        this.profilePicture = profilePicture;
        this.role = role;
        this.status = status;
    }
}
