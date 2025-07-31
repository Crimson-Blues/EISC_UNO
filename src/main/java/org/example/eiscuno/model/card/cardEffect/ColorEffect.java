package org.example.eiscuno.model.cardEffect;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.game.TurnEnum;
import org.example.eiscuno.model.player.Player;

public class ColorEffect implements ICardEffect {

    @Override
    public void applyEffect(CardEffectContext context) {
        Card card = context.getCard();
        String color = context.getColor();
        card.setColor(color);
        context.getGame().changeTurn();
        System.out.println("Se cambio el color a: " + color);
    }
}
