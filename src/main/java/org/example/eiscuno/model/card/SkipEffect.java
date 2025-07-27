package org.example.eiscuno.model.card;

import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

public class SkipEffect implements CardEffect {

    @Override
    public void applyEffect(GameUno game, Player targetPlayer) {
        // No se hace nada, el turno se maneja en el flujo del juego.
        System.out.println("¡Turno saltado para " + targetPlayer.getTypePlayer() + "!");
    }

    @Override
    public void ChangeColorEffect(Card card, String color) {
        //No hacemos nada aquí con skip effect...
    }

}
