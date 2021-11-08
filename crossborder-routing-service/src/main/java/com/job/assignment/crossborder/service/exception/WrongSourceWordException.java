package com.job.assignment.crossborder.service.exception;

public class WrongSourceWordException extends RuntimeException{
    public WrongSourceWordException(String source) {
        super("Wrong source word typed, " +  source);
    }
}