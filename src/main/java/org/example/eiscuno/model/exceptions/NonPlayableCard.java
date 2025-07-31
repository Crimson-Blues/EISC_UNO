package org.example.eiscuno.model.exceptions;

public class NonPlayableCard extends Exception {
    public NonPlayableCard() {
        super("NON PLAYABLE CARD");
    }
    public NonPlayableCard(String message, Throwable cause) {
        super(message, cause);
    }
    public NonPlayableCard(String message) {
        super(message);
    }
    public NonPlayableCard(Throwable cause) {
        super(cause);
    }

}
