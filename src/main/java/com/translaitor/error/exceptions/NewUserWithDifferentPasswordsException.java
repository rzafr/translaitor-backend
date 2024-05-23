package com.translaitor.error.exceptions;

public class NewUserWithDifferentPasswordsException extends RuntimeException {

    private static final long serialVersionUID = -7978601526802035152L;

    public NewUserWithDifferentPasswordsException() {
        super("Passwords do not match");
    }
}
