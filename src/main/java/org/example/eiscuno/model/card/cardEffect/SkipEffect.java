package org.example.eiscuno.model.card.cardEffect;

import org.example.eiscuno.model.card.Card;

import java.io.Serializable;

public class SkipEffect implements ICardEffect, Serializable {

    @Override
    public void applyEffect(Card.CardEffectContext context) {
        System.out.println("¡Turno saltado para " + context.getTargetPlayer().getTypePlayer() + "!");
    }

}
