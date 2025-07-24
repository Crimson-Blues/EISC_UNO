package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUnoMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.Objects;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController {

    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private ImageView tableImageView;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;
    private ThreadPlayMachine threadPlayMachine;
    private ThreadSingUnoMachine  threadSingUnoMachine;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        this.gameUno.startGame();
        printCardsHumanPlayer();
        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView);
        threadPlayMachine.start();

        threadSingUnoMachine = new ThreadSingUnoMachine(humanPlayer.getCardsPlayer());
        Thread thread =  new Thread(threadSingUnoMachine);
        thread.start();
    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);
        Card currentCardOnTable = null;

        //Controlando excepcion
        try {
            currentCardOnTable = this.table.getCurrentCardOnTheTable();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Mesa vacía. Iniciando juego...");
        }

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            // Validar si la carta es jugable
            boolean isPlayable = isCardPlayable(card, currentCardOnTable);

            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                // Aqui deberian verificar si pueden en la tabla jugar esa carta
                if (isPlayable) {
                    if (Objects.equals(card.getValue(), "EAT4")) {
                        gameUno.eatCard(machinePlayer, 4);
                        System.out.println("- La maquina come 4 cartas");
                    }
                    if (Objects.equals(card.getValue(), "EAT2")) {
                        gameUno.eatCard(machinePlayer, 2);
                        System.out.println("- La maquina come 2 cartas");
                    }
                    gameUno.playCard(card);
                    tableImageView.setImage(card.getImage());
                    humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                    printCardsHumanPlayer();
                    threadPlayMachine.setHasPlayerPlayed(true);
                }
            });
            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
    }

    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {
        // Implement logic to take a card here
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        // Implement logic to handle Uno event here
    }

    private boolean isCardPlayable(Card cardToPlay, Card currentCardOnTable) {
        // Si la mesa está vacía (inicio del juego), cualquier carta es válida.
        if (currentCardOnTable == null) {
            return true;
        }

        // Coincidencia en color o valor
        boolean colorMatch = cardToPlay.getColor().equals(currentCardOnTable.getColor());
        boolean valueMatch = cardToPlay.getValue().equals(currentCardOnTable.getValue());

        // Cartas especiales (como "WILD" o "+4") pueden jugarse en cualquier momento
        boolean isSpecialCard = cardToPlay.getValue().equals("NEWCOLOR") ||
                cardToPlay.getValue().equals("EAT4");

        return colorMatch || valueMatch || isSpecialCard;
    }

}
