package edu.tcu.cs.tankbattle.strategies;

import edu.tcu.cs.tankbattle.models.Tank;

import java.util.Random;

public class AIMovement implements MovementStrategy {
    private Random random = new Random();

    @Override
    public void move(Tank tank, double dx, double dy) {
        // Randomize movement direction
        double randomX = random.nextInt(3) - 1; // -1, 0, 1
        double randomY = random.nextInt(3) - 1; // -1, 0, 1
        tank.getImageView().setX(tank.getImageView().getX() + randomX * tank.getSpeed());
        tank.getImageView().setY(tank.getImageView().getY() + randomY * tank.getSpeed());
    }
}
