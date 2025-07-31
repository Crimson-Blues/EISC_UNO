package org.example.eiscuno.model.exceptions;

public class EmptyDeck extends Exception {
    public EmptyDeck() {
        super("Empty Deck");
    }
    public EmptyDeck (String message,  Throwable cause) {
        super(message, cause);
    }
    public EmptyDeck (Throwable cause) {
        super(cause);
    }
    public EmptyDeck(String message) {
        super(message);
    }
}
