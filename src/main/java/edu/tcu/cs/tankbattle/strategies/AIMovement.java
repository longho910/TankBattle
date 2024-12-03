package edu.tcu.cs.tankbattle.strategies;

import edu.tcu.cs.tankbattle.models.Direction;
import edu.tcu.cs.tankbattle.models.Tank;

import java.util.Random;

public class AIMovement implements MovementStrategy {
    private Random random = new Random();
    private Direction currentDirection;
    private int movementCounter = 0; // Counter to control how long a tank moves in one direction
    private final int movementDuration = 60; // Move in one direction for 60 frames (~1 second at 60 FPS)

    @Override
    public void move(Tank tank, double unusedDx, double unusedDy) {
        // Choose a new direction if the counter reaches the duration
        if (movementCounter == 0) {
            currentDirection = getRandomDirection();
            tank.setDirection(currentDirection); // Update tank's direction
        }

        // Move in the current direction
        double speed = tank.getSpeed();
        switch (currentDirection) {
            case UP -> tank.getImageView().setY(tank.getImageView().getY() - speed);
            case DOWN -> tank.getImageView().setY(tank.getImageView().getY() + speed);
            case LEFT -> tank.getImageView().setX(tank.getImageView().getX() - speed);
            case RIGHT -> tank.getImageView().setX(tank.getImageView().getX() + speed);
        }

        // Increment the counter and reset if it exceeds the duration
        movementCounter++;
        if (movementCounter >= movementDuration) {
            movementCounter = 0; // Reset for the next direction change
        }

        // Boundary checks (assuming game area is 800x600)
        double newX = tank.getImageView().getX();
        double newY = tank.getImageView().getY();
        if (newX < 0) tank.getImageView().setX(0);
        if (newX > 760) tank.getImageView().setX(760); // Width minus tank width
        if (newY < 0) tank.getImageView().setY(0);
        if (newY > 560) tank.getImageView().setY(560); // Height minus tank height
    }

    private Direction getRandomDirection() {
        int randomIndex = random.nextInt(Direction.values().length);
        return Direction.values()[randomIndex];
    }
}
