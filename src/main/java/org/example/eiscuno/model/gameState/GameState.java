package org.example.eiscuno.model.gameState;

import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.io.IOException;
import java.io.Serializable;


public class GameState implements Serializable {

    private Deck deck;
    private GameUno gameUno;
    private Table table;
    private Player humanPlayer;
    private Player machinePlayer;

    public GameState(Deck deck, GameUno gameUno, Table table, Player humanPlayer, Player machinePlayer) {
        this.deck = deck;
        this.gameUno = gameUno;
        this.table = table;
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
    }

    public Deck getDeck() {
        return deck;
    }

    public Table getTable() {
        return table;
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public Player getMachinePlayer() {
        return machinePlayer;
    }

    public GameUno getGameUno() {
        return gameUno;
    }
}
