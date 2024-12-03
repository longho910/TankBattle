package edu.tcu.cs.tankbattle.game_elements;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Explosion {
    private final ImageView imageView;
    private int frame = 0; // Start from frame 0
    private Timeline animation;

    public Explosion(double x, double y) {
        this.imageView = new ImageView();
        this.imageView.setX(x);
        this.imageView.setY(y);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void play(Runnable onComplete) {
        // Define the timeline animation
        animation = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (frame <= 10) {
                imageView.setImage(new Image(getClass().getResource("/images/" + frame + ".gif").toExternalForm()));
                frame++;
            } else {
                animation.stop();
                if (onComplete != null) {
                    onComplete.run(); // Execute the callback
                }
            }
        }));

        // Repeat for 11 frames (0 to 10)
        animation.setCycleCount(11);
        animation.play();
    }

}
