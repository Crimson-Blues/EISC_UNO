package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.io.Serializable;

/**
 * Represents a game of Uno.
 * This class manages the game logic and interactions between players, deck, and the table.
 */
public class GameUno implements IGameUno, Serializable {

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private volatile TurnEnum turn;


    /**
     * Constructs a new GameUno instance.
     *
     * @param humanPlayer   The human player participating in the game.
     * @param machinePlayer The machine player participating in the game.
     * @param deck          The deck of cards used in the game.
     * @param table         The table where cards are placed during the game.
     */
    public GameUno(Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
        this.turn = TurnEnum.PLAYER;
    }

    /**
     * Starts the Uno game by distributing cards to players.
     * The human player and the machine player each receive 10 cards from the deck.
     */
    @Override
    public void startGame() {
        //Reparte las cartas iniciales al jugador humano y máquina
        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                humanPlayer.addCard(this.deck.takeCard());
            } else {
                machinePlayer.addCard(this.deck.takeCard());
            }
        }
        putFirstCard();
    }

    /**
     * Verifies is a card satisfy the game rules
     * @param cardToPlay the card being played
     * @param currentCardOnTable the card compared to
     * @return true if it can be played, false if not
     */
    @Override
    public boolean isCardPlayable(Card cardToPlay, Card currentCardOnTable) {
        // Si la mesa está vacía (inicio del juego), cualquier carta es válida.
        if (currentCardOnTable == null) {
            return true;
        }

        // Coincidencia en color o valor
        boolean colorMatch = cardToPlay.getColor().equals(currentCardOnTable.getColor());
        System.out.println(colorMatch);
        boolean valueMatch = cardToPlay.getValue().equals(currentCardOnTable.getValue());
        System.out.println(valueMatch);

        // Cartas especiales (como "WILD" o "+4") pueden jugarse en cualquier momento
        boolean isSpecialCard = cardToPlay.getValue().equals("NEWCOLOR") ||
                cardToPlay.getValue().equals("EAT4");

        return colorMatch || valueMatch || isSpecialCard;
    }

    /**
     * Allows a player to draw a specified number of cards from the deck.
     *
     * @param player        The player who will draw cards.
     * @param numberOfCards The number of cards to draw.
     */
    @Override
    public void eatCard(Player player, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            player.addCard(this.deck.takeCard());
        }
    }

    /**
     * Put the first card on the table
     */
    @Override
    public void putFirstCard(){
        Card cardToPlay = this.deck.viewCard();
        while(cardToPlay.getEffect() != null) {
            this.deck.shuffle();
            cardToPlay = this.deck.viewCard();
        }
        playCard(this.deck.takeCard());
    }

    /**
     * Places a card on the table during the game.
     *
     * @param card The card to be placed on the table.
     */
    @Override
    public void playCard(Card card) {
        this.table.addCardOnTheTable(card);
    }

    @Override
    public void changeTurn() {
        if(turn ==  TurnEnum.PLAYER) {
            turn = TurnEnum.MACHINE;
        } else if(turn ==  TurnEnum.MACHINE) {
            turn = TurnEnum.PLAYER;
        }
    }

    public TurnEnum getTurn() {
        return turn;
    }
    public void setTurn(TurnEnum turn) {
        this.turn = turn;
    }

    /**
     * Handles the scenario when a player shouts "Uno", forcing the other player to draw a card.
     *
     * @param playerWhoSang The player who shouted "Uno".
     */
    @Override
    public void haveSungOne(String playerWhoSang) {
        if (playerWhoSang.equals("HUMAN_PLAYER")) {
            machinePlayer.addCard(this.deck.takeCard());
        } else {
            humanPlayer.addCard(this.deck.takeCard());
        }
    }

    /**
     * Retrieves the current visible cards of the human player starting from a specific position.
     *
     * @param posInitCardToShow The initial position of the cards to show.
     * @return An array of cards visible to the human player.
     */
    @Override
    public Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow) {
        int totalCards = this.humanPlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.humanPlayer.getCard(posInitCardToShow + i);
        }

        return cards;
    }

    /**
     * Checks if the game is over.
     *
     * @return 0 if the game is not over, 1 if the deck is empty, 2 if the human player has
     * played all his card or 3 if the machine player has played all his cards.
     */
    @Override
    public GameStateEnum isGameOver() {
        if(deck.isEmpty()){
            return GameStateEnum.DECK_EMPTY;
        } else if (humanPlayer.getCardsPlayer().isEmpty()) {
            return GameStateEnum.PLAYER_WON;
        } else if (machinePlayer.getCardsPlayer().isEmpty()) {
            return GameStateEnum.MACHINE_WON;

        }
        return GameStateEnum.GAME_ONGOING;
    }

    public Deck getDeck(){
        return this.deck;
    }
}
