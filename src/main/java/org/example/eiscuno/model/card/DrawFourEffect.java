package org.example.eiscuno.model.card;

import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.io.Serializable;

public class DrawFourEffect implements CardEffect, Serializable {

    @Override
    public void applyEffect(GameUno game, Player targetPlayer) {
        game.eatCard(targetPlayer, 4);
        System.out.println(targetPlayer.getTypePlayer() + " roba 4 cartas.");
    }

    @Override
    public void ChangeColorEffect(Card card, String color) {
        card.setColor(color);
    }
}
