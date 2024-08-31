package com.agridence.microservice.Assignment.controller;

import com.agridence.microservice.Assignment.dto.NoteDTO;
import com.agridence.microservice.Assignment.model.Note;
import com.agridence.microservice.Assignment.service.NoteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@Tag(name = "Note", description = "Note management APIs")
public class NoteController {

    private NoteService noteService;

    @Autowired
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    @Operation(summary = "Create a new note", description = "Creates a new note for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Note created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<Note> addNote(Authentication authentication, @RequestBody NoteDTO noteDTO) {
        String username = authentication.getName();
        Note addedNote = noteService.addNote(username, noteDTO);
        return ResponseEntity.ok(addedNote);
    }

    @GetMapping
    @Operation(summary = "Get all notes", description = "Retrieves all notes for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Notes retrieved successfully")
    public ResponseEntity<List<Note>> getUserNotes(Authentication authentication) {
        String username = authentication.getName();
        List<Note> userNotes = noteService.getUserNotes(username);
        return ResponseEntity.ok(userNotes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a note by ID", description = "Retrieves a specific note by its ID for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Note retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Note not found")
    public ResponseEntity<Note> getNoteById(Authentication authentication, @PathVariable Long id) {
        String username = authentication.getName();
        Note note = noteService.getNoteById(username, id);
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a note", description = "Deletes a specific note by its ID for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Note deleted successfully")
    @ApiResponse(responseCode = "404", description = "Note not found")
    public ResponseEntity<?> deleteNote(Authentication authentication, @PathVariable Long id) {
        String username = authentication.getName();
        noteService.deleteNote(username, id);
        return ResponseEntity.ok().build();
    }
}