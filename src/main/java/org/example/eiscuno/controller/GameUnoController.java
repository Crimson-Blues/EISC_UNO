package org.example.eiscuno.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.Serializable.SerializableFileHandler;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.card.cardEffect.CardEffectContext;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameStateEnum;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.game.TurnEnum;
import org.example.eiscuno.model.gameState.GameState;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUnoMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.example.eiscuno.view.WelcomeStage;

import java.io.IOException;
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

    @FXML
    private ImageView deckImageView;

    @FXML
    private ImageView deckCards;

    @FXML
    private Button unoBotton;

    @FXML
    private ImageView unoImageView;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;
    private ThreadPlayMachine threadPlayMachine;
    private ThreadSingUnoMachine  threadSingUnoMachine;
    private Thread threadSingUno;


    private GameState gameState;
    private Boolean isContinue;
    private SerializableFileHandler serializableFileHandler;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() throws IOException {
        //initVariables();
        isContinue = WelcomeStage.getInstance().getWelcomeStageController().returnIsContinue();
        WelcomeStage.deleteInstance();
        serializableFileHandler = new SerializableFileHandler();

        if(!isContinue){
            initVariables();
            this.gameUno.startGame();
            printCardsHumanPlayer();
            printCardsMachinePlayer();
            threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.gameUno, this.humanPlayer);
            threadPlayMachine.start();

            threadSingUnoMachine = new ThreadSingUnoMachine(this.humanPlayer, this.gameUno);
            threadSingUno = new Thread(threadSingUnoMachine);
            threadSingUno.setDaemon(true);
            threadSingUno.start();

            setUnoListener();
            setGameOverListener();
            showUnoBotton();
        }
        else{
            loadGameState();
        }
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
            tableImageView.setImage(currentCardOnTable.getImage());
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
                if (isPlayable && gameUno.getTurn() == TurnEnum.PLAYER && gameUno.isGameOver() == GameStateEnum.GAME_ONGOING) {

                    String color = "";
                    // Aplicar efecto si es una carta especial
                    Player targetPlayer = machinePlayer;
                    if (card.getValue().equals("NEWCOLOR") || card.getValue().equals("EAT4")) {
                        color = askColor();
                    }
                    if(card.getEffect()!=null){
                        card.applyEffect(new CardEffectContext(gameUno, targetPlayer, card, color));
                    }else{
                        gameUno.changeTurn();
                        System.out.println("Turn: " + gameUno.getTurn());
                    }
                    gameUno.playCard(card);
                    saveGameState();
                    tableImageView.setImage(card.getImage());
                    humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                    printCardsHumanPlayer();
                }
            });
            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
            if(gameUno.isGameOver() != GameStateEnum.GAME_ONGOING){
                threadPlayMachine.stopThread();
                threadPlayMachine.interrupt();

                threadSingUnoMachine.stopThread();
                threadSingUno.interrupt();
                gameHasEndedAlert();
            }
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
    private void printCardsMachinePlayer(){
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
     * Notifies the controller if the uno button should appear or dissappear and if his functionality should work because
     * 1) The machine sang uno
     * 2) The player sang uno
     * 3) The player no longer has one card
     */
    private void setUnoListener(){
        threadSingUnoMachine.setUnoEventListener(() -> {
            Platform.runLater(() -> {
                showUnoBotton();
                printCardsHumanPlayer();
                printCardsMachinePlayer();
            });
        });

        threadPlayMachine.setUnoEventListener(() -> {
            Platform.runLater(() -> {
                showUnoBotton();
                printCardsHumanPlayer();
                printCardsMachinePlayer();
            });
        });
    }

    /**
     * Notifies the controller if the game has ended in the turn of the machine (the machine played all his cards) or if the cards ran out
     */
    private void setGameOverListener(){
        threadPlayMachine.setGameOverListener(() -> {
            Platform.runLater(() -> {
                if(gameUno.isGameOver() != GameStateEnum.GAME_ONGOING){
                    threadPlayMachine.stopThread();
                    threadPlayMachine.interrupt();

                    threadSingUnoMachine.stopThread();
                    threadSingUno.interrupt();

                    gameHasEndedAlert();
                }
            });
        });

        deck.setGameOverListener(() -> {
            if(gameUno.isGameOver() != GameStateEnum.GAME_ONGOING){
                threadPlayMachine.stopThread();
                threadPlayMachine.interrupt();

                threadSingUnoMachine.stopThread();
                threadSingUno.interrupt();

                deckImageView.setVisible(false);
                gameHasEndedAlert();
            }
        });
    }

    /**
     * Shows a visual alert if the game has ended
     */

    private void gameHasEndedAlert(){
        GameStateEnum gameState = gameUno.isGameOver();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Juego terminado!");

        String winnerName;

        switch (gameState) {
            case PLAYER_WON:
                winnerName = "Jugador humano";
                alert.setHeaderText("🎉 ¡Tenemos un ganador! 🎉");
                alert.setContentText("El ganador es el: " + winnerName);
                break;
            case MACHINE_WON:
                winnerName = "Jugador máquina";
                alert.setHeaderText("🎉 ¡Tenemos un ganador! 🎉");
                alert.setContentText("El ganador es el: " + winnerName);
                break;
            case DECK_EMPTY:
                alert.setHeaderText("Se acabaron las cartas...  :(");
                alert.setContentText("No hay ganador. El juego ha terminado");

        }

        ImageView imageView = new ImageView(new Image(getClass().getResource("/org/example/eiscuno/favicon.png").toString()));
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
        alert.setGraphic(imageView);

        alert.showAndWait();

    }

    /**
     * Shows the uno button depending on the rules
     */

    private void showUnoBotton(){
        if(humanPlayer.getCardsPlayer().size() == 1 && !threadSingUnoMachine.getAlreadySangUno()
            && gameUno.isGameOver() == GameStateEnum.GAME_ONGOING){
            unoBotton.setVisible(true);
            unoBotton.setManaged(true);

            unoImageView.setVisible(true);
            unoImageView.setManaged(true);
        }else{
            unoBotton.setVisible(false);
            unoBotton.setManaged(false);

            unoImageView.setVisible(false);
            unoImageView.setManaged(false);
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
        boolean areCardsPlayable = false;

        for(int i=0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            Card card = this.humanPlayer.getCardsPlayer().get(i);
            if(gameUno.isCardPlayable(card, table.getCurrentCardOnTheTable())){
                areCardsPlayable = true;
                break;
            }
        }
        if(!areCardsPlayable && gameUno.isGameOver() == GameStateEnum.GAME_ONGOING){
            gameUno.eatCard(humanPlayer, 1);
            saveGameState();
            showUnoBotton();
            gameUno.changeTurn();
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        System.out.println("Cantar UNO presionado");

        if(humanPlayer.getCardsPlayer().size() == 1  && !threadSingUnoMachine.getAlreadySangUno()){
            threadSingUnoMachine.setAlreadySangUno(true);
            saveGameState();
        }

        showUnoBotton();
    }

    public void saveGameState(){
        System.out.println("Saving gameState...");
        this.gameState = new GameState(this.deck,this.gameUno,this.table,this.humanPlayer,this.machinePlayer);
        serializableFileHandler.serialize("GameState.ser", gameState);
    }

    public void loadGameState(){
        System.out.println("Loading gameState...");
        this.gameState = (GameState) serializableFileHandler.deserialize("GameState.ser");
        if(gameState != null){
            this.deck = gameState.getDeck();
            this.gameUno = gameState.getGameUno();
            this.table = gameState.getTable();
            this.humanPlayer = gameState.getHumanPlayer();
            this.machinePlayer = gameState.getMachinePlayer();

            printCardsHumanPlayer();
            printCardsMachinePlayer();

            Card cardOnTable = table.getCurrentCardOnTheTable();
            if (cardOnTable != null) {
                tableImageView.setImage(cardOnTable.getImage());
            }

            threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.gameUno, this.humanPlayer);
            threadPlayMachine.start();

            threadSingUnoMachine = new ThreadSingUnoMachine(this.humanPlayer, this.gameUno);
            threadSingUno = new Thread(threadSingUnoMachine);
            threadSingUno.setDaemon(true);
            threadSingUno.start();

            setUnoListener();
            setGameOverListener();
            showUnoBotton();
        }
    }
}
