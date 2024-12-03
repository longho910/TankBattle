package edu.tcu.cs.tankbattle.factories;

import edu.tcu.cs.tankbattle.models.*;
import edu.tcu.cs.tankbattle.strategies.AIMovement;
import edu.tcu.cs.tankbattle.strategies.PlayerMovement;

public class GameObjectFactory {
    public static Tank createTank(String type, double x, double y) {
        if (type.equals("player")) {
            return new Tank("/images/HtankD.gif", 100, new PlayerMovement(), x, y);
        } else if (type.equals("enemy")) {
            return new Tank("/images/HtankD2.gif", 50, new AIMovement(), x, y);
        }
        return null;
    }

    public static Wall createWall(double x, double y) {
        return new Wall("/images/commonWall.gif", x, y);
    }

    public static Missile createMissile(String type, double x, double y) {
        String imagePath = "/images/bulletD.gif";
        return new Missile(imagePath, type, x, y, 10);
    }

    public static MedPack createMedPack(double x, double y) {
        return new MedPack("/images/hp.png", x, y);
    }
}
