package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.GameUnoStage;

public class ThreadPlayMachine extends Thread{
    private Table table;
    private Player playerMachine;
    private ImageView tableImageView;
    private volatile Boolean hasPlayerPlayed;

    public ThreadPlayMachine(Table table, Player playerMachine, ImageView tableImageView) {
        this.table = table;
        this.playerMachine = playerMachine;
        this.tableImageView = tableImageView;
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
        int index = (int) (Math.random() * playerMachine.getCardsPlayer().size());
        System.out.println("Playing card: " + index);
        Card card = playerMachine.getCardsPlayer().get(index);

        table.addCardOnTheTable(card);
        tableImageView.setImage(card.getImage());
    }
    public void setHasPlayerPlayed(Boolean hasPlayerPlayed){
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
}
