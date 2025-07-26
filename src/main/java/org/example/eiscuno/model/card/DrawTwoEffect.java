package org.example.eiscuno.model.card;


import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

public class DrawTwoEffect  implements CardEffect {

    @Override
    public void applyEffect(GameUno game, Player targetPlayer) {
        game.eatCard(targetPlayer, 2);
        System.out.println(targetPlayer.getTypePlayer() + " roba 2 cartas.");
    }

    @Override
    public void ChangeColorEffect(String newColor) {
        // No hay necesidad de cambiar color.
    }
}
