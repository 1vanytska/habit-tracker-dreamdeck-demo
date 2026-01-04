package com.htdd.habittrackerdreamdeckdemo.goal;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.htdd.habittrackerdreamdeckdemo.step.Step;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "goal")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "goal_id")
    private UUID goalId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String picture;

    private String description;

    @NotNull
    @Column(name = "category_id") // nullable = false
    private UUID categoryId;

    @NotNull
    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate deadline;

    @NotBlank
    @Column(nullable = false)
    private String status;

    @NotNull
    @Column(name = "is_archived", nullable = false)
    private boolean isArchived;

    @Column(name = "archiving_time")
    private LocalDate archivingTime;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Step> steps = new ArrayList<>();

    public Goal(UUID userId, String title, String picture, String description,
                UUID categoryId, boolean isPublic, LocalDate startDate, LocalDate deadline,
                String status, boolean isArchived, LocalDate archivingTime) {
        this.userId = userId;
        this.title = title;
        this.picture = picture;
        this.description = description;
        this.categoryId = categoryId;
        this.isPublic = isPublic;
        this.startDate = startDate;
        this.deadline = deadline;
        this.status = status;
        this.isArchived = isArchived;
        this.archivingTime = archivingTime;
    }
}
