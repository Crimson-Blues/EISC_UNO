package org.example.eiscuno.model.card.cardEffect;

import org.example.eiscuno.model.card.Card;

import java.io.Serializable;

public class DrawFourEffect implements ICardEffect, Serializable {

    @Override
    public void applyEffect(Card.CardEffectContext context) {
        try {
            context.getGame().eatCard(context.getTargetPlayer(), 4);
        }catch (Exception e){
            e.printStackTrace();
        }

        context.getCard().setColor(context.getColor());
        System.out.println(context.getTargetPlayer().getTypePlayer() + " roba 4 cartas.");
    }
}

