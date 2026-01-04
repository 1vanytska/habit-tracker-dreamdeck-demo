package com.htdd.habittrackerdreamdeckdemo.step;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.htdd.habittrackerdreamdeckdemo.goal.Goal;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "step")
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "step_id")
    private UUID stepId;

    @Column(nullable = false)
    private String description;

    @JsonProperty("isCompleted")
    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "goal_id", nullable = false)
    @JsonBackReference
    private Goal goal;

    public Step(String description, Goal goal) {
        this.description = description;
        this.goal = goal;
        this.isCompleted = false;
    }
}