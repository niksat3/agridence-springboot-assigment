package com.agridence.microservice.Assignment.dto;

public class NoteDTO {
    private String title;
    private String content;

    // Getters
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}