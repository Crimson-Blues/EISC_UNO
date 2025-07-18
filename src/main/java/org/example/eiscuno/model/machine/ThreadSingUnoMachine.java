package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;

public class ThreadSingUnoMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;


    public ThreadSingUnoMachine(ArrayList<Card> cardsPlayer){
        this.cardsPlayer = cardsPlayer;
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep((long) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            hasHumanOneCard();

        }
    }

    public void hasHumanOneCard(){
        if(cardsPlayer.size() == 1){
            System.out.println("UNO!");
        }
    }
}
