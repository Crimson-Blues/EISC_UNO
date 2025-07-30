package org.example.eiscuno.model.card;

import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.io.Serializable;

public class ColorEffect implements CardEffect, Serializable {
    private String newColor;

    public ColorEffect(String newColor) {
        this.newColor = newColor;
    }

    @Override
    public void applyEffect(GameUno game, Player targetPlayer) {
        //Nada...

    }

    @Override
    public void ChangeColorEffect(Card card, String color) {
        System.out.println("Se cambio el color a: " + color);
        card.setColor(color);
    }
}
