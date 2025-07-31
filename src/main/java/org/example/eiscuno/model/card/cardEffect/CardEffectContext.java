package org.example.eiscuno.model.card.cardEffect;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.io.Serializable;

public class CardEffectContext implements Serializable {
    private GameUno game;
    private Player targetPlayer;
    private Card card;
    private String color;

    public CardEffectContext(GameUno game, Player targetPlayer) {
        this.game = game;
        this.targetPlayer = targetPlayer;
        this.card = null;
        this.color = null;
    }

    public CardEffectContext(Card card, String color) {
        this.card = card;
        this.color = color;
        this.game = null;
        this.targetPlayer = null;
    }

    public  CardEffectContext(GameUno game, Player targetPlayer, Card card, String color) {
        this.game = game;
        this.targetPlayer = targetPlayer;
        this.card = card;
        this.color = color;
    }

    public GameUno getGame() {
        return game;
    }

    public void setGame(GameUno game) {
        this.game = game;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
