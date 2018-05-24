package com.example.roronoa.trialapp;

public class Note {

    private  String subject , content;

    Note() {

    }

    Note(String subject , String content) {
        this.subject = subject;
        this.content = content;
    }

    public String getSubject() {
        return  subject;
    }

    public String getContent() {
        return content;
    }
}
