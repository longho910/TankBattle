package edu.tcu.cs.tankbattle.strategies;

import edu.tcu.cs.tankbattle.models.Tank;

public interface MovementStrategy {
    void move(Tank tank, double dx, double dy);
}
