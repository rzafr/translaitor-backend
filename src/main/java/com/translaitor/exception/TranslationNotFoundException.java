package com.translaitor.exception;

import javax.persistence.EntityNotFoundException;

public class TranslationNotFoundException extends EntityNotFoundException {

    public TranslationNotFoundException() {
        super("The translation could not be found");
    }

    public TranslationNotFoundException(Long id) {
        super(String.format("The translation with id %d could not be found", id));
    }
}
