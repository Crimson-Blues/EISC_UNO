package org.example.eiscuno.model.card.cardEffect;


import java.io.Serializable;

public class DrawTwoEffect  implements ICardEffect, Serializable {

    @Override
    public void applyEffect(CardEffectContext context) {
        context.getGame().eatCard(context.getTargetPlayer(), 2);
        System.out.println(context.getTargetPlayer().getTypePlayer() + " roba 2 cartas.");
    }

}
