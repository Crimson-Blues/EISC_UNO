package org.example.eiscuno.model.card.cardEffect;

import org.example.eiscuno.model.card.Card;

public interface ICardEffect {
    //Efectos
    void applyEffect(Card.CardEffectContext context);
}
