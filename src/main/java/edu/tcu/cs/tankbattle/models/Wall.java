package edu.tcu.cs.tankbattle.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Wall {
    private ImageView imageView;

    public Wall(String imagePath, double x, double y) {
        this.imageView = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
        this.imageView.setX(x);
        this.imageView.setY(y);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
