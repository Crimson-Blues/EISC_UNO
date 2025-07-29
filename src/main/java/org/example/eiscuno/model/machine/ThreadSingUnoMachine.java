package org.example.eiscuno.model.machine;

import org.example.eiscuno.listener.UnoEventListener;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;

public class ThreadSingUnoMachine implements Runnable{
    private volatile boolean alreadySangUno;
    private GameUno gameUno;
    private Player humanPlayer;
    private UnoEventListener listener;



    public ThreadSingUnoMachine(Player humanPlayer, GameUno gameUno) {
        alreadySangUno = false;
        this.humanPlayer = humanPlayer;
        this.gameUno = gameUno;
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

    public void setUnoEventListener(UnoEventListener listener) {
        this.listener = listener;
    }
    
    public void setAlreadySangUno(boolean alreadySangUno){
        this.alreadySangUno = alreadySangUno;
    }

    public boolean getAlreadySangUno(){
        return this.alreadySangUno;
    }
    
    public void hasHumanOneCard(){
        if(humanPlayer.getCardsPlayer().size() == 1 && !alreadySangUno){
            System.out.println("UNO!");
            gameUno.eatCard(humanPlayer, 1);

            if (listener != null) {
                listener.onPlayerForgotToSayUno();
            }

            setAlreadySangUno(true);
        }
    }
}
