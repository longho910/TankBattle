package edu.tcu.cs.tankbattle.game_elements;

import edu.tcu.cs.tankbattle.strategies.MovementStrategy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

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
        this.speed = 10; // Default speed multiplier
        this.direction = Direction.UP; // Default direction
        this.type = type; // Type of tank (player or enemy)
    }

    public void move(double dx, double dy, List<Wall> walls) {
        // Calculate the tank's next position
        double nextX = this.imageView.getX() + dx * this.speed;
        double nextY = this.imageView.getY() + dy * this.speed;

        // Simulate the movement to check for collisions
        ImageView tempImageView = new ImageView(this.imageView.getImage());
        tempImageView.setX(nextX);
        tempImageView.setY(nextY);

        // Check if the tank collides with any wall
        boolean collides = walls.stream().anyMatch(wall ->
                tempImageView.getBoundsInParent().intersects(wall.getImageView().getBoundsInParent())
        );

        // If no collision, update the tank's position
        if (!collides) {
            this.imageView.setX(nextX);
            this.imageView.setY(nextY);
        }
    }


    public void setDirection(Direction direction)         {
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

    public MovementStrategy getMovementStrategy() {
        return movementStrategy;
    }



}
