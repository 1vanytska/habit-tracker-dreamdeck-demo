package com.htdd.habittrackerdreamdeckdemo.note;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/goal/{goalId}")
    public ResponseEntity<List<Note>> getNotesByGoalId(@PathVariable UUID goalId) {
        return ResponseEntity.ok(noteService.getNotesByGoalId(goalId));
    }

    @PostMapping
    public ResponseEntity<Note> saveNote(@Valid @RequestBody Note note) {
        return ResponseEntity.ok(noteService.saveNote(note));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable UUID id) {
        noteService.deleteNoteById(id);
        return ResponseEntity.noContent().build();
    }
}