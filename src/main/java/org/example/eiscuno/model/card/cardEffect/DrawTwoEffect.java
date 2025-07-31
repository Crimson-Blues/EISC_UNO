package org.example.eiscuno.model.card.cardEffect;


import org.example.eiscuno.model.card.Card;

import java.io.Serializable;

public class DrawTwoEffect  implements ICardEffect, Serializable {

    @Override
    public void applyEffect(Card.CardEffectContext context) {
        try {
            context.getGame().eatCard(context.getTargetPlayer(), 2);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(context.getTargetPlayer().getTypePlayer() + " roba 2 cartas.");
    }

}
