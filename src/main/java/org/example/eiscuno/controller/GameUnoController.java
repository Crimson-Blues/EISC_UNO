package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.cardEffect.CardEffectContext;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUnoMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

import java.util.List;
import java.util.Optional;

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
        printCardsMachinePlayer();
        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.gameUno);
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

        //Controlando excepción
        try {
            currentCardOnTable = this.table.getCurrentCardOnTheTable();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Mesa vacía...");
        }

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            Card finalCurrentCardOnTable = currentCardOnTable;
            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                boolean isPlayable = gameUno.isCardPlayable(card, finalCurrentCardOnTable);
                System.out.println(isPlayable);
                if (isPlayable) {
                    gameUno.playCard(card);
                    tableImageView.setImage(card.getImage());
                    humanPlayer.removeCard(findPosCardsHumanPlayer(card));

                    // Aplicar efecto si es una carta especial
                    Player targetPlayer = machinePlayer;
                    if (card.getValue().equals("EAT4") || card.getValue().equals("EAT2") ||
                            card.getValue().equals("SKIP")) {
                        card.applyEffect(new CardEffectContext(gameUno, targetPlayer));
                        if (card.getValue().equals("EAT4")) {
                            String color = askColor();
                            card.applyEffect(new CardEffectContext(gameUno, targetPlayer, card, color));
                        }

                    }

                    if (card.getValue().equals("NEWCOLOR")) {
                        String color = askColor();
                        card.applyEffect(new CardEffectContext(card, color));
                    }
                    printCardsHumanPlayer();
                    threadPlayMachine.setHasPlayerPlayed(true);
                }
            });
            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }

    }

    //Mostrar mini-ventana para preguntar el color a cambiar
    public String askColor (){
        while (true) {
            ChoiceDialog<String> dialog = new ChoiceDialog<>("GREEN", List.of("GREEN", "YELLOW", "BLUE", "RED"));
            dialog.setTitle("Cambio de color");
            dialog.setHeaderText("Elige un nuevo color");
            dialog.setContentText("Color:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                return result.get();
            }
        }
    }

    /**
     * Prints the cards of the machine (face down).
     */
    void printCardsMachinePlayer(){
        this.gridPaneCardsMachine.getChildren().clear();
        int maxCards = Math.min(this.machinePlayer.getCardsPlayer().size(), 4);

        for(int i=0; i < maxCards; i++) {
            ImageView backCardUno = new ImageView(EISCUnoEnum.CARD_UNO.getFilePath());
            backCardUno.setY(16);
            backCardUno.setFitHeight(90);
            backCardUno.setFitWidth(60);

            this.gridPaneCardsMachine.add(backCardUno, i, 0);
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

}
