package org.example.eiscuno.model.machine;

import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.ImageView;
import org.example.eiscuno.listener.UnoEventListener;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.*;

public class ThreadPlayMachine extends Thread{
    private Table table;
    private Player playerMachine;
    private Player humanPlayer;
    private ImageView tableImageView;
    private GameUno gameUno;
    private volatile Boolean hasPlayerPlayed;
    private UnoEventListener listener;


    public ThreadPlayMachine(Table table, Player playerMachine, ImageView tableImageView, GameUno gameUno
    , Player HumanPlayer) {
        this.table = table;
        this.playerMachine = playerMachine;
        this.tableImageView = tableImageView;
        this.gameUno = gameUno;
        this.hasPlayerPlayed = false;
        this.humanPlayer = HumanPlayer;
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

                card.applyEffect(gameUno, humanPlayer);

                if(card.getValue().equals("SKIP") || card.getValue().equals("REVERSE")) {
                    setHasPlayerPlayed(true);
                }else{
                    setHasPlayerPlayed(false);
                }

                if(card.getValue().equals("NEWCOLOR")) {
                    List<String> colors = List.of("GREEN", "YELLOW", "BLUE", "RED");
                    Random random = new Random();
                    String chosenColor = colors.get(random.nextInt(colors.size()));

                    System.out.println("[MÁQUINA] Color elegido: " + chosenColor);
                    card.getEffect().ChangeColorEffect(card, chosenColor);
                }else if (card.getValue().equals("EAT4")) {
                    Card previousCard = table.getpreviousCardOnTheTable();
                    System.out.println("La carta anterior tenia un color de: " + previousCard.getColor());
                    card.getEffect().ChangeColorEffect(card,previousCard.getColor());
                    System.out.println(card.getValue());
                    System.out.println("La carta +4 obtiene el color de: " + card.getColor());

                }
                tableImageView.setImage(card.getImage());
                playerMachine.removeCard(i);

                if (listener != null) {
                    listener.onPlayerForgotToSayUno();
                }

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

    public void setUnoEventListener(UnoEventListener listener) {
        this.listener = listener;
    }

    public boolean getHasPlayerPlayed(){
        return hasPlayerPlayed;
    }
}
