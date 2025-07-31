package org.example.eiscuno.model.card.cardEffect;

public class CardEffect {
    private ICardEffect cardEffect;

    public CardEffect(){
        cardEffect = null;
    }

    public CardEffect(ICardEffect cardEffect) {
        this.cardEffect = cardEffect;
    }

    public void setCardEffect(ICardEffect cardEffect) {
        this.cardEffect = cardEffect;
    }

    public void applyEffect(CardEffectContext context) {
        if(cardEffect != null){
            cardEffect.applyEffect(context);
        }
    }

    public ICardEffect getCardEffect(){
        return cardEffect;
    }
}
