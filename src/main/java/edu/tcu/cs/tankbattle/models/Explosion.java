package edu.tcu.cs.tankbattle.models;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Explosion {
    private ImageView imageView;
    private int frame = 0;

    public Explosion(double x, double y) {
        this.imageView = new ImageView();
        this.imageView.setX(x);
        this.imageView.setY(y);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void play() {
        AnimationTimer animation = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (frame <= 10) {
                    imageView.setImage(new Image(getClass().getResource("/images/" + frame + ".gif").toExternalForm()));
                    frame++;
                } else {
                    stop();
                }
            }
        };
        animation.start();
    }
}
