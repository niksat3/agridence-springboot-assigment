package com.agridence.microservice.Assignment.service;

import com.agridence.microservice.Assignment.dto.NoteDTO;
import com.agridence.microservice.Assignment.model.Note;
import com.agridence.microservice.Assignment.model.User;
import com.agridence.microservice.Assignment.repository.NoteRepository;
import com.agridence.microservice.Assignment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addNote_Success() {
        User user = new User();
        user.setUsername("testuser");

        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle("Test Note");
        noteDTO.setContent("This is a test note");

        Note savedNote = new Note();
        savedNote.setId(1L);
        savedNote.setTitle("Test Note");
        savedNote.setContent("This is a test note");
        savedNote.setUser(user);

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(noteRepository.save(any(Note.class))).thenReturn(savedNote);

        Note result = noteService.addNote("testuser", noteDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Note", result.getTitle());
        assertEquals("This is a test note", result.getContent());
        assertEquals(user, result.getUser());

        verify(userRepository).findByUsername("testuser");
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void addNote_UserNotFound() {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle("Test Note");
        noteDTO.setContent("This is a test note");

        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> noteService.addNote("nonexistent", noteDTO));

        verify(userRepository).findByUsername("nonexistent");
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    void getUserNotes_Success() {
        User user = new User();
        user.setUsername("testuser");

        Note note1 = new Note();
        note1.setId(1L);
        note1.setTitle("Note 1");
        Note note2 = new Note();
        note2.setId(2L);
        note2.setTitle("Note 2");

        List<Note> notes = Arrays.asList(note1, note2);

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(noteRepository.findByUser(user)).thenReturn(notes);

        List<Note> result = noteService.getUserNotes("testuser");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Note 1", result.get(0).getTitle());
        assertEquals("Note 2", result.get(1).getTitle());

        verify(userRepository).findByUsername("testuser");
        verify(noteRepository).findByUser(user);
    }

    @Test
    void getNoteById_Success() {
        User user = new User();
        user.setUsername("testuser");

        Note note = new Note();
        note.setId(1L);
        note.setTitle("Test Note");
        note.setUser(user);

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(noteRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(note));

        Note result = noteService.getNoteById("testuser", 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Note", result.getTitle());

        verify(userRepository).findByUsername("testuser");
        verify(noteRepository).findByIdAndUser(1L, user);
    }

    @Test
    void deleteNote_Success() {
        User user = new User();
        user.setUsername("testuser");

        Note note = new Note();
        note.setId(1L);
        note.setTitle("Test Note");
        note.setUser(user);

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(noteRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(note));

        noteService.deleteNote("testuser", 1L);

        verify(userRepository).findByUsername("testuser");
        verify(noteRepository).findByIdAndUser(1L, user);
        verify(noteRepository).delete(note);
    }
}