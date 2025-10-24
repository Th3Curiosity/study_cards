package com.th3curiosity.studycards.exceptions;

import com.th3curiosity.studycards.dto.other.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeckNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDeckNotFound(DeckNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("DECK_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    public ResponseEntity<ErrorResponse> handleLoginDataException(InvalidUsernameOrPasswordException ex) {
        ErrorResponse error = new ErrorResponse("INVALID_USERNAME_OR_PASSWORD", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}