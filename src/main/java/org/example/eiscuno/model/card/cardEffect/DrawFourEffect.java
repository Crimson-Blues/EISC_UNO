package org.example.eiscuno.model.card.cardEffect;

import java.io.Serializable;

public class DrawFourEffect implements ICardEffect, Serializable {

    @Override
    public void applyEffect(CardEffectContext context) {
        context.getGame().eatCard(context.getTargetPlayer(), 4);
        context.getCard().setColor(context.getColor());
        System.out.println(context.getTargetPlayer().getTypePlayer() + " roba 4 cartas.");
    }
}

