package edu.tcu.cs.tankbattle.strategies;

import edu.tcu.cs.tankbattle.game_elements.Tank;

public class PlayerMovement implements MovementStrategy {
    @Override
    public void move(Tank tank, double dx, double dy) {
        tank.getImageView().setX(tank.getImageView().getX() + dx);
        tank.getImageView().setY(tank.getImageView().getY() + dy);
    }
}
