package com.agridence.microservice.Assignment.repository;

import com.agridence.microservice.Assignment.model.Note;
import com.agridence.microservice.Assignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser(User user);
    Optional<Note> findByIdAndUser(Long id, User user);
}