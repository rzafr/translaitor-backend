package com.translaitor.exception;

import javax.persistence.EntityNotFoundException;

public class EmptyTranslationListException extends EntityNotFoundException {

    public EmptyTranslationListException() {
        super("No translations were found with the search criteria");
    }
}
