package com.htdd.habittrackerdreamdeckdemo.note;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void saveNote_Positive_ShouldUpdateExistingNote_WhenNoteExists() {
        UUID goalId = UUID.randomUUID();
        LocalDate date = LocalDate.now();

        Note existingNote = new Note();
        existingNote.setGoalId(goalId);
        existingNote.setDate(date);
        existingNote.setContent("Старий текст");

        Note newNoteData = new Note();
        newNoteData.setGoalId(goalId);
        newNoteData.setDate(date);
        newNoteData.setContent("Новий текст");

        when(noteRepository.findByGoalIdAndDate(goalId, date)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(existingNote)).thenReturn(existingNote);

        Note result = noteService.saveNote(newNoteData);

        assertEquals("Новий текст", result.getContent());
        verify(noteRepository, times(1)).save(existingNote);
    }

    @Test
    void saveNote_Positive_ShouldCreateNewNote_WhenNoteDoesNotExist() {
        UUID goalId = UUID.randomUUID();
        LocalDate date = LocalDate.now();

        Note newNote = new Note();
        newNote.setGoalId(goalId);
        newNote.setDate(date);
        newNote.setContent("Зовсім нова нотатка");

        when(noteRepository.findByGoalIdAndDate(goalId, date)).thenReturn(Optional.empty());
        when(noteRepository.save(newNote)).thenReturn(newNote);

        Note result = noteService.saveNote(newNote);

        assertEquals("Зовсім нова нотатка", result.getContent());
        verify(noteRepository, times(1)).save(newNote);
    }

    @Test
    void deleteNoteById_Positive_ShouldCallRepositoryDelete() {
        UUID noteId = UUID.randomUUID();

        noteService.deleteNoteById(noteId);

        verify(noteRepository, times(1)).deleteById(noteId);
    }
}