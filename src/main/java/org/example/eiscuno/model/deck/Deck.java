package org.example.eiscuno.model.deck;

import org.example.eiscuno.listener.GameOverListener;
import org.example.eiscuno.listener.UnoEventListener;
import org.example.eiscuno.model.card.*;
import org.example.eiscuno.model.cardEffect.ColorEffect;
import org.example.eiscuno.model.cardEffect.DrawFourEffect;
import org.example.eiscuno.model.cardEffect.DrawTwoEffect;
import org.example.eiscuno.model.cardEffect.SkipEffect;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

import java.util.Collections;
import java.util.Stack;

/**
 * Represents a deck of Uno cards.
 */
public class Deck {
    private Stack<Card> deckOfCards;
    private GameOverListener gameOverListener;

    /**
     * Constructs a new deck of Uno cards and initializes it.
     */
    public Deck() {
        deckOfCards = new Stack<>();
        initializeDeck();
    }


    /**
     * Initializes the deck with cards based on the EISCUnoEnum values.
     */
    private void initializeDeck() {
        for (EISCUnoEnum cardEnum : EISCUnoEnum.values()) {
            if (cardEnum.name().startsWith("GREEN_") ||
                    cardEnum.name().startsWith("YELLOW_") ||
                    cardEnum.name().startsWith("BLUE_") ||
                    cardEnum.name().startsWith("RED_") ||
                    cardEnum.name().startsWith("SKIP_") ||
                    cardEnum.name().startsWith("RESERVE_") ||
                    cardEnum.name().startsWith("TWO_WILD_DRAW_") ||
                    cardEnum.name().equals("FOUR_WILD_DRAW") ||
                    cardEnum.name().equals("WILD")) {
                Card card = new Card(cardEnum.getFilePath(), getCardValue(cardEnum.name()), getCardColor(cardEnum.name()));
            if (card.getValue() != null) {
                switch (card.getValue()) {
                    case "SKIP":
                        card.setEffect(new SkipEffect());
                        break;
                    case "NEWCOLOR":
                        card.setEffect(new ColorEffect());
                        break;
                    case "EAT2":
                        card.setEffect(new DrawTwoEffect());
                        break;
                    case "EAT4":
                        card.setEffect(new DrawFourEffect());
                        break;
                }
            } else {
                System.out.println("Invalid card value");
            }
            deckOfCards.push(card);
        }
        Collections.shuffle(deckOfCards);
    }
}

    private String getCardValue(String name) {
        if (name.endsWith("0")){
            return "0";
        } else if (name.endsWith("1")){
            return "1";
        } else if (name.endsWith("2")){
            return "2";
        } else if (name.endsWith("3")){
            return "3";
        } else if (name.endsWith("4")){
            return "4";
        } else if (name.endsWith("5")){
            return "5";
        } else if (name.endsWith("6")){
            return "6";
        } else if (name.endsWith("7")){
            return "7";
        } else if (name.endsWith("8")){
            return "8";
        } else if (name.endsWith("9")){
            return "9";   //VALORES PARA CONTROLAR MAS FACIL LOS CONDICIONALES DE ESTAS CARTAS ESPECIALES
        } else if (name.startsWith("TWO_WILD_DRAW")) {
            return "EAT2";//comer 2
        } else if (name.startsWith("WILD")) {
            return "NEWCOLOR";//cambiar color
        } else if (name.startsWith("RESERVE")) {
            return "REVERSE"; //reverse card
        } else if (name.startsWith("SKIP")) {
            return "SKIP";//bloquear turno
        } else if (name.startsWith("FOUR_WILD_DRAW")) {
            return "EAT4"; //comer 4
        } else {
            return "UNKNOWN";
        }

    }

    private String getCardColor(String name){
        if(name.startsWith("GREEN")){
            return "GREEN";
        } else if(name.startsWith("YELLOW")){
            return "YELLOW";
        } else if(name.startsWith("BLUE")){
            return "BLUE";
        } else if(name.startsWith("RED")) {
            return "RED";
        } else if (name.endsWith("GREEN")) {
            return "GREEN";//comer 2
        } else if (name.endsWith("YELLOW")) {
            return "YELLOW";//cambiar color
        } else if (name.endsWith("BLUE")) {
            return "BLUE"; //reverse card
        } else if (name.endsWith("RED")) {
            return "RED";//bloquear turno
        } else if (name.startsWith("WILD")) {
                return "GREEN";
        } else if (name.startsWith("EAT4")) {
            return "GREEN";
        } else {
            return "UNKNOWN";
        }
    }

    /**
     * Takes a card from the top of the deck.
     *
     * @return the card from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card takeCard() {
        if (deckOfCards.isEmpty()) {

            if (gameOverListener != null) {
                gameOverListener.onGameOver();
            }

            throw new IllegalStateException("No hay más cartas en el mazo.");

        }
        return deckOfCards.pop();
    }

    /**
     * Shows card at the top of the deck without taking it out of the deck.
     *
     * @return Reference to card from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card viewCard() {
        if (deckOfCards.isEmpty()) {
            throw new IllegalStateException("No hay cartas en el mazo.");

        }

        return deckOfCards.peek();
    }

    public void shuffle(){
        Collections.shuffle(deckOfCards);
    }

    /**
     * Sets the listener that will be notified when the game ends.
     * <p>
     * This method allows the controller or other components to be informed when the game reaches
     * a terminal state (e.g., no more cards in the deck or a player wins).
     * This listener will trigger visual updates such as showing a game-over alert.
     * </p>
     *
     * @param gameOverListener the listener to be notified when the game ends
     */
    public void setGameOverListener(GameOverListener gameOverListener) {
        this.gameOverListener = gameOverListener;
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return deckOfCards.isEmpty();
    }
}
