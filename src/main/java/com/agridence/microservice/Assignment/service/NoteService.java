package com.agridence.microservice.Assignment.service;

import com.agridence.microservice.Assignment.dto.NoteDTO;
import com.agridence.microservice.Assignment.model.Note;
import com.agridence.microservice.Assignment.model.User;
import com.agridence.microservice.Assignment.repository.NoteRepository;
import com.agridence.microservice.Assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    public Note addNote(String username, NoteDTO noteDTO) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Note note = new Note();
        note.setTitle(noteDTO.getTitle());
        note.setContent(noteDTO.getContent());
        note.setUser(user);

        return noteRepository.save(note);
    }

    public List<Note> getUserNotes(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return noteRepository.findByUser(user);
    }

    public Note getNoteById(String username, Long noteId) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Optional<Note> note = noteRepository.findByIdAndUser(noteId, user);
        return note.orElseThrow(() -> new RuntimeException("Note not found or does not belong to user"));
    }

    public void deleteNote(String username, Long noteId) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Optional<Note> note = noteRepository.findByIdAndUser(noteId, user);
        if (note.isPresent()) {
            noteRepository.delete(note.get());
        } else {
            throw new RuntimeException("Note not found or does not belong to user");
        }
    }
}