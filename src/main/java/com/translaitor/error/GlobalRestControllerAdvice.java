package com.translaitor.error;

import com.translaitor.error.model.impl.ApiErrorImpl;
import com.translaitor.error.model.impl.ApiValidationSubError;
import com.translaitor.exception.EmptyTranslationListException;
import com.translaitor.exception.TranslationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalRestControllerAdvice {

    /*@ExceptionHandler(NewUserWithDifferentPasswordsException.class)
    public ResponseEntity<ApiErrorImpl> handleNewUserErrors(Exception ex) {
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        ApiErrorImpl apiError = new ApiErrorImpl(status, ex.getMessage());
        return ResponseEntity.status(status).headers(headers).body(apiError);
    }

    private ResponseEntity<ApiErrorImpl> buildErrorResponseEntity(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ApiErrorImpl.builder()
                        .status(status)
                        .message(message)
                        .build());

    }*/

    @ExceptionHandler({TranslationNotFoundException.class, EmptyTranslationListException.class})
    public ResponseEntity<?> handleNotFoundException(EntityNotFoundException exception, WebRequest request) {
        return buildApiError(exception.getMessage(), request, HttpStatus.NOT_FOUND);
    }

    private final ResponseEntity<Object> buildApiError(String message, WebRequest request, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(
                        ApiErrorImpl.builder()
                                .status(status)
                                .message(message)
                                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                                .build()
                );
    }

    private final ResponseEntity<Object> buildApiErrorWithSubErrors(String message, WebRequest request, HttpStatus status, List<ObjectError> subErrors) {
        return ResponseEntity
                .status(status)
                .body(
                        ApiErrorImpl.builder()
                                .status(status)
                                .message(message)
                                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                                .subErrors(subErrors.stream()
                                        .map(ApiValidationSubError::fromObjectError)
                                        .collect(Collectors.toList())
                                )
                                .build()
                        // TODO Combinar con el anterior
                );

    }
}
