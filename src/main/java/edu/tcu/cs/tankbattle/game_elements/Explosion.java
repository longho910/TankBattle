package edu.tcu.cs.tankbattle.game_elements;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Explosion {
    private final ImageView imageView;
    private int frame = 0; // Start from frame 0
    private final Timeline animation;

    public Explosion(double x, double y) {
        this.imageView = new ImageView();
        this.imageView.setX(x);
        this.imageView.setY(y);

        // Initialize the animation
        animation = new Timeline(new KeyFrame(Duration.millis(50), e -> updateFrame()));
        animation.setCycleCount(11); // 11 frames (0 to 10)
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void play(Runnable onComplete) {
        animation.setOnFinished(e -> {
            // Remove the explosion ImageView from the game pane
            if (onComplete != null) {
                onComplete.run();
            }
        });
        animation.play();
    }

    private void updateFrame() {
        if (frame <= 10) {
            // Update the explosion image
            imageView.setImage(new Image(getClass().getResource("/images/" + frame + ".gif").toExternalForm()));
            frame++;
        }
    }
}
