package com.htdd.habittrackerdreamdeckdemo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Goal")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID goalId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String title;

    private String picture;

    private String description;

    private UUID categoryId;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private boolean isArchived;

    private LocalDate archivationTime;

    public Goal(UUID userId, String title, String picture, String description,
                UUID categoryId, boolean isPublic, LocalDate deadline,
                String status, boolean isArchived, LocalDate archivationTime) {
        this.userId = userId;
        this.title = title;
        this.picture = picture;
        this.description = description;
        this.categoryId = categoryId;
        this.isPublic = isPublic;
        this.deadline = deadline;
        this.status = status;
        this.isArchived = isArchived;
        this.archivationTime = archivationTime;
    }
}
