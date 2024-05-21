package com.translaitor.error.exceptions;

public class NewUserWithDifferentPasswordsException extends RuntimeException {

    public NewUserWithDifferentPasswordsException() {
        super("Passwords do not match");
    }
}
