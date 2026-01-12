package com.htdd.habittrackerdreamdeckdemo.note;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "note_id")
    private UUID noteId;

    @NotNull
    @Column(name = "goal_id", nullable = false)
    private UUID goalId;

    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;

    public Note(UUID goalId, LocalDate date, String content) {
        this.goalId = goalId;
        this.date = date;
        this.content = content;
    }
}