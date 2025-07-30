package org.example.eiscuno.model.cardEffect;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

public class SkipEffect implements ICardEffect {

    @Override
    public void applyEffect(CardEffectContext context) {
        System.out.println("¡Turno saltado para " + context.getTargetPlayer().getTypePlayer() + "!");
    }

}
