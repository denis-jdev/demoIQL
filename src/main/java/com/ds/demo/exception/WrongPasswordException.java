package com.ds.demo.exception;

public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException(final String message) {
        super(message);
    }

}
