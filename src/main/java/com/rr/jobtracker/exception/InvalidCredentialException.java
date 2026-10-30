package com.rr.jobtracker.exception;

public class InvalidCredentialException extends RuntimeException{
    public InvalidCredentialException(String message){
        super(message);
    }
}
