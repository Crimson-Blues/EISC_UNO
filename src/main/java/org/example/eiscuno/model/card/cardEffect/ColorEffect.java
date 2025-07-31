package org.example.eiscuno.model.card.cardEffect;

import org.example.eiscuno.model.card.Card;

import java.io.Serializable;

public class ColorEffect implements ICardEffect, Serializable {

    @Override
    public void applyEffect(Card.CardEffectContext context) {
        Card card = context.getCard();
        String color = context.getColor();
        card.setColor(color);
        context.getGame().changeTurn();
        System.out.println("Se cambio el color a: " + color);
    }
}
