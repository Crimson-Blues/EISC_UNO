package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

/**
 * Represents a card in the Uno game.
 */
public class Card  {
    private String url;
    private String value;
    private String color;
    private Image image;
    private ImageView cardImageView;
    private CardEffect effect;

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
    }

    //Nuevo atributo, su efecto
    public void setEffect(CardEffect effect) {
        this.effect = effect;
    }

    public void getEffect(){
        System.out.println(effect);
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
        return cardImageView;
    }

    /**
     * Gets the image of the card.
     *
     * @return the Image of the card
     */
    public Image getImage() {
        return image;
    }

    public String getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }


    // Si ya no tiene un efecto asignado, el efecto será
    public void applyEffect(GameUno game, Player targetPlayer) {
        if (effect != null) {
            effect.applyEffect(game, targetPlayer);
        }
    }

}
