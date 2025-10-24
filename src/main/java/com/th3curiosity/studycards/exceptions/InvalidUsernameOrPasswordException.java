package com.th3curiosity.studycards.exceptions;

public class InvalidUsernameOrPasswordException extends RuntimeException {
    public InvalidUsernameOrPasswordException(String message) {
        super(message);
    }
    public InvalidUsernameOrPasswordException() {
        super("InvalidUsernameOrPassword");
    }
}
