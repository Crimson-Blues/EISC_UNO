package org.example.eiscuno.model.card;


import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.io.Serializable;

public class DrawTwoEffect  implements CardEffect, Serializable {

    @Override
    public void applyEffect(GameUno game, Player targetPlayer) {
        game.eatCard(targetPlayer, 2);
        System.out.println(targetPlayer.getTypePlayer() + " roba 2 cartas.");
    }

    @Override
    public void ChangeColorEffect(Card card, String color) {
        // No hay necesidad de cambiar color.
    }
}
