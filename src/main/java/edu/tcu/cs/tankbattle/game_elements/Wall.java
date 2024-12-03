package edu.tcu.cs.tankbattle.game_elements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Wall {
    private ImageView imageView;
    private boolean destroyable;


    public Wall(String imagePath, boolean destroyable, double x, double y) {
        this.imageView = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
        this.imageView.setX(x);
        this.imageView.setY(y);
        this.destroyable = destroyable;
    }

    public boolean isDestroyable() {
        return destroyable;
    }
    public ImageView getImageView() {
        return imageView;
    }
}
