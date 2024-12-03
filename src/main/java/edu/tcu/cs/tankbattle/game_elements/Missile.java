package edu.tcu.cs.tankbattle.game_elements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Missile {
    private ImageView imageView;
    private double speed;
    private double dx;
    private double dy;

    private String type; // Type of missile (e.g., "player" or "enemy")


    public Missile(String imagePath, String type, double startX, double startY, double speed) {
        this.imageView = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
        this.imageView.setX(startX);
        this.imageView.setY(startY);
        this.speed = speed;
        this.type = type; // Assign the type
        this.dx = 0;
        this.dy = 0; // Default movement
    }

    public void move(double dx, double dy) {
        imageView.setX(imageView.getX() + dx * speed);
        imageView.setY(imageView.getY() + dy * speed);
    }

    public void setMovement(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        imageView.setX(imageView.getX() + dx * speed);
        imageView.setY(imageView.getY() + dy * speed);
    }

    public ImageView getImageView() {
        return imageView;
    }
    public String getType() {
        return type; // Return the type of missile
    }
}
