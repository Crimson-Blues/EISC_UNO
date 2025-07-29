package org.example.eiscuno.model.cardEffect;


import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

public class DrawTwoEffect  implements ICardEffect {

    @Override
    public void applyEffect(CardEffectContext context) {
        context.getGame().eatCard(context.getTargetPlayer(), 2);
        System.out.println(context.getTargetPlayer().getTypePlayer() + " roba 2 cartas.");
    }

}
