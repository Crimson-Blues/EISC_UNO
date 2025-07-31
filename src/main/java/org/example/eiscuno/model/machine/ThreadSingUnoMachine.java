package org.example.eiscuno.model.machine;

import org.example.eiscuno.listener.UnoEventListener;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.exceptions.EmptyDeck;
import org.example.eiscuno.model.game.GameStateEnum;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;

public class ThreadSingUnoMachine implements Runnable{
    private volatile boolean alreadySangUno;
    private volatile boolean running;
    private GameUno gameUno;
    private Player humanPlayer;
    private UnoEventListener listener;


    /**
     * Constructor that initializes the thread with the human player and game instance.
     *
     * @param humanPlayer The human player.
     * @param gameUno     The game logic instance.
     */
    public ThreadSingUnoMachine(Player humanPlayer, GameUno gameUno) {
        alreadySangUno = false;
        running = true;
        this.humanPlayer = humanPlayer;
        this.gameUno = gameUno;
    }

    /**
     * The main execution method for the thread.
     * It periodically checks if the human player has only one card and hasn't declared "UNO".
     */
    @Override
    public void run() {
        while(running){
            if(this.humanPlayer.getCardsPlayer().size() == 1 && !alreadySangUno) {
                try {
                    Thread.sleep((long) (2000 + Math.random() * 2000));
                    System.out.println("Sleeping waiting for player to say UNO");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!alreadySangUno){
                    hasHumanOneCard();
                }

            }
        }
    }

    /**
     * Stops the thread's execution.
     */
    public void stopThread(){
        running = false;
    }

    /**
     * Sets the listener to be notified when the player forgets to say "UNO".
     *
     * @param listener The listener instance.
     */
    public void setUnoEventListener(UnoEventListener listener) {
        this.listener = listener;
    }

    /**
     * Sets the flag indicating whether the player has already declared "UNO".
     *
     * @param alreadySangUno True if the player already said "UNO", false otherwise.
     */
    public void setAlreadySangUno(boolean alreadySangUno){
        this.alreadySangUno = alreadySangUno;
    }

    /**
     * Returns whether the player has already declared "UNO".
     *
     * @return True if the player said "UNO", false otherwise.
     */
    public boolean getAlreadySangUno(){
        return this.alreadySangUno;
    }

    /**
     * Checks if the human player has only one card and has not declared "UNO".
     * If so, applies a penalty and notifies the listener.
     */
    public void hasHumanOneCard(){
        System.out.println("UNO!");
        try {
            gameUno.eatCard(humanPlayer, 1);
        }catch (EmptyDeck e){
            e.printStackTrace();
        }

        if (listener != null) {
            listener.onPlayerForgotToSayUno();
        }
        setAlreadySangUno(true);
    }
}
