package com.agridence.microservice.Assignment.controller;

import com.agridence.microservice.Assignment.dto.NoteDTO;
import com.agridence.microservice.Assignment.model.Note;
import com.agridence.microservice.Assignment.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NoteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NoteService noteService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private NoteController noteController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
        when(authentication.getName()).thenReturn("testuser");
    }

    @Test
    void addNote_Success() throws Exception {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle("Test Note");
        noteDTO.setContent("This is a test note");

        Note addedNote = new Note();
        addedNote.setId(1L);
        addedNote.setTitle("Test Note");
        addedNote.setContent("This is a test note");

        when(noteService.addNote(eq("testuser"), any(NoteDTO.class))).thenReturn(addedNote);

        mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteDTO))
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Note"))
                .andExpect(jsonPath("$.content").value("This is a test note"));

        verify(noteService).addNote(eq("testuser"), any(NoteDTO.class));
    }

    @Test
    void getUserNotes_Success() throws Exception {
        Note note1 = new Note();
        note1.setId(1L);
        note1.setTitle("Note 1");
        Note note2 = new Note();
        note2.setId(2L);
        note2.setTitle("Note 2");
        List<Note> notes = Arrays.asList(note1, note2);

        when(noteService.getUserNotes("testuser")).thenReturn(notes);

        mockMvc.perform(get("/api/notes")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Note 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Note 2"));

        verify(noteService).getUserNotes("testuser");
    }

    @Test
    void getNoteById_Success() throws Exception {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Test Note");
        note.setContent("This is a test note");

        when(noteService.getNoteById("testuser", 1L)).thenReturn(note);

        mockMvc.perform(get("/api/notes/1")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Note"))
                .andExpect(jsonPath("$.content").value("This is a test note"));

        verify(noteService).getNoteById("testuser", 1L);
    }

    @Test
    void deleteNote_Success() throws Exception {
        doNothing().when(noteService).deleteNote("testuser", 1L);

        mockMvc.perform(delete("/api/notes/1")
                .principal(authentication))
                .andExpect(status().isOk());

        verify(noteService).deleteNote("testuser", 1L);
    }
}