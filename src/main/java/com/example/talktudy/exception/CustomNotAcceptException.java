package com.example.talktudy.exception;

public class CustomNotAcceptException extends RuntimeException {
    public CustomNotAcceptException(String message) {
        super(message);
    }
}