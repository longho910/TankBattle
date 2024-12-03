package edu.tcu.cs.tankbattle.strategies;

import edu.tcu.cs.tankbattle.models.Tank;

import java.util.Random;

public class AIMovement implements MovementStrategy {
    private Random random = new Random();

    @Override
    public void move(Tank tank, double dx, double dy) {
        int randomSpeedFactor = random.nextInt(3) + 1; // Randomize AI speed
        tank.getImageView().setX(tank.getImageView().getX() + dx * randomSpeedFactor);
        tank.getImageView().setY(tank.getImageView().getY() + dy * randomSpeedFactor);
    }
}
