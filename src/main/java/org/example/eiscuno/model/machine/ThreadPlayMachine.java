package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.ArrayList;
import java.util.Collections;

public class ThreadPlayMachine extends Thread{
    private Table table;
    private Player playerMachine;
    private ImageView tableImageView;
    private GameUno gameUno;
    private volatile Boolean hasPlayerPlayed;


    public ThreadPlayMachine(Table table, Player playerMachine, ImageView tableImageView, GameUno gameUno) {
        this.table = table;
        this.playerMachine = playerMachine;
        this.tableImageView = tableImageView;
        this.gameUno = gameUno;
        this.hasPlayerPlayed = false;
    }
    public void run(){
        while(true){
            if(hasPlayerPlayed) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                putCardOnTable();
                hasPlayerPlayed = false;
            }
        }
    }

    public void putCardOnTable(){
        ArrayList<Card> cards = new ArrayList<>(playerMachine.getCardsPlayer());
        Card cardOnTable = table.getCurrentCardOnTheTable();

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);

            if (gameUno.isCardPlayable(card, cardOnTable)) {
                gameUno.playCard(card);
                tableImageView.setImage(card.getImage());
                playerMachine.removeCard(i);
                return;
            }
        }

        gameUno.eatCard(playerMachine, 1);
        System.out.println("No hay cartas jugables.");
        putCardOnTable();

    }

    public void setHasPlayerPlayed(Boolean hasPlayerPlayed){
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
}
