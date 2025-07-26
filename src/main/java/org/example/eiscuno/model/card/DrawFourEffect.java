package org.example.eiscuno.model.card;

import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

public class DrawFourEffect implements CardEffect {

    @Override
    public void applyEffect(GameUno game, Player targetPlayer) {
        game.eatCard(targetPlayer, 4);
        System.out.println(targetPlayer.getTypePlayer() + " roba 4 cartas.");
    }

    @Override
    public void ChangeColorEffect(String newColor) {
        //Cambiar el color
    }
}
