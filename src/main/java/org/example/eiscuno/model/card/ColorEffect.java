package org.example.eiscuno.model.card;

import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

public class ColorEffect implements CardEffect {
    private String newColor;

    public ColorEffect(String newColor) {
        this.newColor = newColor;
    }

    @Override
    public void applyEffect(GameUno game, Player targetPlayer) {
        // Actualiza el color de la carta en la mesa (simulado).
        System.out.println("Color cambiado a: " + newColor);
    }

    @Override
    public void ChangeColorEffect(String newColor) {
        this.newColor = newColor;
    }
}
