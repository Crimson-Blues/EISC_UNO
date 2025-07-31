package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.cardEffect.CardEffect;
import org.example.eiscuno.model.card.cardEffect.ICardEffect;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.io.Serializable;

/**
 * Represents a card in the Uno game.
 */
public class Card implements Serializable {
    private String url;
    private String value;
    private String color;
    private transient Image image;
    private transient ImageView cardImageView;
    private CardEffect effect;

    public class CardEffectContext implements Serializable {
        private GameUno game;
        private Player targetPlayer;
        private String color;

        public CardEffectContext(GameUno game, Player targetPlayer) {
            this.game = game;
            this.targetPlayer = targetPlayer;
            this.color = null;
        }

        public CardEffectContext(String color) {
            this.color = color;
            this.game = null;
            this.targetPlayer = null;
        }

        public  CardEffectContext(GameUno game, Player targetPlayer, String color) {
            this.game = game;
            this.targetPlayer = targetPlayer;
            this.color = color;
        }

        public GameUno getGame() {
            return game;
        }

        public void setGame(GameUno game) {
            this.game = game;
        }

        public Player getTargetPlayer() {
            return targetPlayer;
        }

        public void setTargetPlayer(Player targetPlayer) {
            this.targetPlayer = targetPlayer;
        }

        public Card getCard() {
            return Card.this;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    /**
     * Constructs a Card with the specified image URL and name.
     *
     * @param url   the URL of the card image
     * @param value of the card
     */
    public Card(String url, String value, String color) {
        this.url = url;
        this.value = value;
        this.color = color;
        this.image = new Image(String.valueOf(getClass().getResource(url)));
        this.cardImageView = createCardImageView();
        this.effect = new CardEffect();
    }

    //Nuevo atributo, su efecto
    public void setEffect(ICardEffect effect) {
        this.effect.setCardEffect(effect);
    }

    public ICardEffect getEffect(){
        return this.effect.getCardEffect();
    }

    /**
     * Creates and configures the ImageView for the card.
     *
     * @return the configured ImageView of the card
     */
    private ImageView createCardImageView() {
        ImageView card = new ImageView(this.image);
        card.setY(16);
        card.setFitHeight(90);
        card.setFitWidth(70);
        return card;
    }

    /**
     * Gets the ImageView representation of the card.
     *
     * @return the ImageView of the card
     */
    public ImageView getCard() {
        if (cardImageView == null || image == null) {
            this.image = new Image(String.valueOf(getClass().getResource(url)));
            this.cardImageView = createCardImageView();
        }
        return cardImageView;
    }

    /**
     * Gets the image of the card.
     *
     * @return the Image of the card
     */
    public Image getImage() {
        if (image == null) {
            this.image = new Image(String.valueOf(getClass().getResource(url)));
        }
        return image;
    }

    public String getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // Si ya no tiene un efecto asignado, el efecto será
    public void applyEffect(Card.CardEffectContext context) {
        if (effect != null) {
            effect.applyEffect(context);
        }
    }

}
