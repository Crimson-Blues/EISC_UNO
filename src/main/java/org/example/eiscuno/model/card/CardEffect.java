package org.example.eiscuno.model.card;

import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.io.Serializable;

public interface CardEffect {
    // Efecto "SKIP" (Ceder turno)
    void applyEffect(GameUno game, Player targetPlayer);

    // Efecto "NEWCOLOR" (Cambio de color)
    void ChangeColorEffect(Card card, String color);

}
