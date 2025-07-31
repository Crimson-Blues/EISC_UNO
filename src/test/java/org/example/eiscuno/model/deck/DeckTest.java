package org.example.eiscuno.model.deck;

import javafx.application.Platform;
import org.example.eiscuno.listener.GameOverListener;
import org.example.eiscuno.model.card.Card;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @BeforeAll
    public static void initJFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @Test
    void deckCreatesCorrectly(){
        Deck deck = new Deck();
        assertNotNull(deck);
        assertFalse(deck.isEmpty());
        assertEquals(54, deck.getDeckOfCards().size());
    }

    @Test
    void deckNotifiesCorrectlyWhenItIsEmpty(){
        Deck deck = new Deck();
        assertFalse(deck.isEmpty());
        while (!deck.isEmpty()){
            deck.takeCard();
        }
        assertTrue(deck.isEmpty());
    }

    @Test
    void deckTakesCardsCorrectly(){
        Deck deck = new Deck();
        int initialSize = 0;
        while (!deck.isEmpty()) {
            deck.takeCard();
            initialSize++;
        }
        assertThrows(IllegalStateException.class, deck::takeCard);
        assertEquals(54, initialSize);
    }

    @Test
    void viewCardWorksAsExpected(){
        Deck deck = new Deck();
        Card top = deck.viewCard();
        Card firstTakenCard = deck.takeCard();
        assertSame(top, firstTakenCard);

        while (!deck.isEmpty()){
            deck.takeCard();
        }

        assertThrows(IllegalStateException.class, deck::viewCard);
    }



}