package com.th3curiosity.studycards.exceptions;

public class DeckNotFoundException extends RuntimeException {
    public DeckNotFoundException(String message) {
        super(message);
    }

    public DeckNotFoundException(Long deckId, String username) {
        super(String.format("Deck with id %d not found for user %s", deckId, username));
    }
}