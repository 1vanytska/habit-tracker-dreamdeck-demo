package com.htdd.habittrackerdreamdeckdemo.note;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public List<Note> getNotesByGoalId(UUID goalId) {
        return noteRepository.findByGoalId(goalId);
    }

    public Note saveNote(Note note) {
        Optional<Note> existingNote = noteRepository.findByGoalIdAndDate(note.getGoalId(), note.getDate());

        if (existingNote.isPresent()) {
            Note noteToUpdate = existingNote.get();
            noteToUpdate.setContent(note.getContent());
            return noteRepository.save(noteToUpdate);
        } else {
            return noteRepository.save(note);
        }
    }

    public void deleteNoteById(UUID noteId) {
        noteRepository.deleteById(noteId);
    }
}