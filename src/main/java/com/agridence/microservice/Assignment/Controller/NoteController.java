package com.agridence.microservice.Assignment.controller;

import com.agridence.microservice.Assignment.dto.NoteDTO;
import com.agridence.microservice.Assignment.model.Note;
import com.agridence.microservice.Assignment.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private NoteService noteService;

    @Autowired
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<Note> addNote(Authentication authentication, @RequestBody NoteDTO noteDTO) {
        String username = authentication.getName();
        Note addedNote = noteService.addNote(username, noteDTO);
        return ResponseEntity.ok(addedNote);
    }

    @GetMapping
    public ResponseEntity<List<Note>> getUserNotes(Authentication authentication) {
        String username = authentication.getName();
        List<Note> userNotes = noteService.getUserNotes(username);
        return ResponseEntity.ok(userNotes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(Authentication authentication, @PathVariable Long id) {
        String username = authentication.getName();
        Note note = noteService.getNoteById(username, id);
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(Authentication authentication, @PathVariable Long id) {
        String username = authentication.getName();
        noteService.deleteNote(username, id);
        return ResponseEntity.ok().build();
    }
}