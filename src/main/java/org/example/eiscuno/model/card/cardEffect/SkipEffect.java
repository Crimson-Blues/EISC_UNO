package org.example.eiscuno.model.card.cardEffect;

import java.io.Serializable;

public class SkipEffect implements ICardEffect, Serializable {

    @Override
    public void applyEffect(CardEffectContext context) {
        System.out.println("¡Turno saltado para " + context.getTargetPlayer().getTypePlayer() + "!");
    }

}
