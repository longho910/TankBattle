package edu.tcu.cs.tankbattle.models;

import edu.tcu.cs.tankbattle.strategies.MovementStrategy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tank {
    private ImageView imageView;
    private int health;
    private MovementStrategy movementStrategy;
    private int speed;
    private Direction direction; // Current direction of the tank
    private String type; // "player" or "enemy"




    public Tank(String imagePath, String type, int health, MovementStrategy movementStrategy, double startX, double startY) {
        this.imageView = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
        this.imageView.setX(startX);
        this.imageView.setY(startY);
        this.health = health;
        this.movementStrategy = movementStrategy;
        this.speed = 8; // Default speed multiplier
        this.direction = Direction.UP; // Default direction
        this.type = type; // Type of tank (player or enemy)
    }

    public void move(double dx, double dy) {
        movementStrategy.move(this, dx * speed, dy * speed);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        // Update the tank image based on the direction and type
        String suffix = type.equals("enemy") ? "2.gif" : ".gif"; // Enemy tanks have "2.gif"
        String imagePath = switch (direction) {
            case UP -> "/images/HtankU" + suffix;
            case DOWN -> "/images/HtankD" + suffix;
            case LEFT -> "/images/HtankL" + suffix;
            case RIGHT -> "/images/HtankR" + suffix;
        };
        this.imageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
    }

    public Direction getDirection() {
        return direction;
    }
    public void setSpeed(int speed) {
        this.speed = speed; // Allow dynamic speed adjustment
    }

    public int getSpeed() {
        return speed;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getHealth() {
        return health;
    }


}
