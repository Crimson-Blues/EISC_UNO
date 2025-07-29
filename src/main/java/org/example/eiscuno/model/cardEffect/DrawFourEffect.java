package org.example.eiscuno.model.cardEffect;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

public class DrawFourEffect implements ICardEffect {

    @Override
    public void applyEffect(CardEffectContext context) {
        context.getGame().eatCard(context.getTargetPlayer(), 4);
        context.getCard().setColor(context.getColor());
        System.out.println(context.getTargetPlayer().getTypePlayer() + " roba 4 cartas.");
    }
}

