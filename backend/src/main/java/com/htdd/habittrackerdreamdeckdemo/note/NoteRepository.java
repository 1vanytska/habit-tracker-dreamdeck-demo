package com.htdd.habittrackerdreamdeckdemo.note;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID> {
    List<Note> findByGoalId(UUID goalId);

    Optional<Note> findByGoalIdAndDate(UUID goalId, LocalDate date);
}