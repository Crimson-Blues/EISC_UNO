package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.listener.GameOverListener;
import org.example.eiscuno.listener.UnoEventListener;
import org.example.eiscuno.model.Serializable.SerializableFileHandler;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.cardEffect.CardEffectContext;
import org.example.eiscuno.model.game.GameStateEnum;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.gameState.GameState;
import org.example.eiscuno.model.game.TurnEnum;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.*;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player playerMachine;
    private Player humanPlayer;
    private ImageView tableImageView;
    private GameUno gameUno;
    private volatile TurnEnum turn;
    private volatile boolean running;
    private UnoEventListener unoEventListener;
    private GameOverListener gameOverListener;
    private GameState gameState;
    private SerializableFileHandler serializableFileHandler;


    /**
     * Thread responsible for handling the machine player's behavior during the UNO game.
     * It periodically checks whether the human player has played and, if so, attempts to play a valid card.
     */
    public ThreadPlayMachine(Table table, Player playerMachine, ImageView tableImageView, GameUno gameUno
            , Player HumanPlayer) {
        this.table = table;
        this.playerMachine = playerMachine;
        this.tableImageView = tableImageView;
        this.gameUno = gameUno;
        this.turn = gameUno.getTurn();
        this.running = true;
        this.humanPlayer = HumanPlayer;
    }


    /**
     * Main execution loop of the thread.
     * Continuously runs while {@code running} is true. If the human player has played,
     * the machine waits for a short period before attempting to play a card.
     */
    public void run() {
        while (running) {
            turn = gameUno.getTurn();
            if (turn == TurnEnum.MACHINE) {
                System.out.println("MACHINE TURN!");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                putCardOnTable();
            }
        }
    }


    /**
     * Stops the execution loop by setting {@code running} to false.
     */
    public void stopThread() {
        this.running = false;
    }

    /**
     * Handles the logic for the machine player to play a card.
     * Iterates through the machine's hand and plays the first valid card.
     * Applies the card's effect, updates the UI, and notifies listeners.
     * If no card can be played, the machine draws a card and tries again recursively.
     */
    public void putCardOnTable() {
        ArrayList<Card> cards = playerMachine.getCardsPlayer();
        Card cardOnTable = table.getCurrentCardOnTheTable();
        boolean machinePlayed = false;

            if (gameUno.isGameOver() != GameStateEnum.GAME_ONGOING) {
                return;
            }

            for (int i = 0; i < cards.size(); i++) {
                Card card = cards.get(i);
                if (gameUno.isCardPlayable(card, cardOnTable)) {
                    if (card.getValue().equals("NEWCOLOR") || card.getValue().equals("EAT4")) {
                        List<String> colors = List.of("GREEN", "YELLOW", "BLUE", "RED");
                        Random random = new Random();
                        String chosenColor = colors.get(random.nextInt(colors.size()));

                        System.out.println("[MÁQUINA] Color elegido: " + chosenColor);
                        card.applyEffect(new CardEffectContext(gameUno, humanPlayer, card, chosenColor));

                    }else{
                        card.applyEffect(new CardEffectContext(gameUno, humanPlayer));
                    }

                    if(card.getEffect()==null){
                        gameUno.changeTurn();
                    }

                    gameUno.playCard(card);
                    //setHasPlayerPlayed(card.getValue().equals("SKIP") || card.getValue().equals("REVERSE"));

                    tableImageView.setImage(card.getImage());
                    playerMachine.removeCard(i);

                    if (unoEventListener != null) {
                        unoEventListener.onPlayerForgotToSayUno();
                    }

                    if (gameOverListener != null) {
                        gameOverListener.onGameOver();
                    }
                    //Boolean flag to exit while loop
                    machinePlayed = true;

                }
            }
            if(!machinePlayed){
                gameUno.eatCard(playerMachine, 1);
                gameUno.changeTurn();
                System.out.println("No hay cartas jugables.");
            }
                return;
            }
        }

        gameUno.eatCard(playerMachine, 1);
        System.out.println("No hay cartas jugables.");
        saveGameState();
        putCardOnTable();

    }

    }


    /**
     * Sets the listener to be triggered when the machine player should react to a "UNO" event.
     *
     * @param unoEventListener the listener to handle UNO-related behavior
     */
    public void setUnoEventListener(UnoEventListener unoEventListener) {
        this.unoEventListener = unoEventListener;
    }

    /**
     * Sets the listener to be notified when the game ends.
     *
     * @param gameOverListener the listener to handle game over behavior
     */
    public void setGameOverListener(GameOverListener gameOverListener) {
        this.gameOverListener = gameOverListener;
    }


    /**
     * Returns whether the human player has played their turn.
     *
     * @return true if the human player has played, false otherwise
     */
    public boolean getHasPlayerPlayed(){
        return hasPlayerPlayed;
    }

    public void saveGameState(){
        serializableFileHandler = new SerializableFileHandler();
        this.gameState = new GameState(this.gameUno.getDeck(),this.gameUno,this.table,this.humanPlayer,this.playerMachine);
        serializableFileHandler.serialize("GameState.ser",this.gameState);
        System.out.println("Saving machine movement...");
    }
}

