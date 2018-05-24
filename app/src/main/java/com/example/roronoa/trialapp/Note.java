package com.example.roronoa.trialapp;

public class Note {

    private  String mSubject , mContent;

    public Note() {

    }

    public Note(String subject , String content) {
        mSubject = subject;
        mContent = content;
    }

    public String getSubject() {
        return  mSubject;
    }

    public String getContent() {
        return mContent;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
