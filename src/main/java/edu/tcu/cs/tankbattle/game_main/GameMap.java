package edu.tcu.cs.tankbattle.game_main;

import edu.tcu.cs.tankbattle.utils.GameObjectFactory;
import edu.tcu.cs.tankbattle.game_elements.*;
import javafx.scene.layout.Pane;

import java.util.List;

public class GameMap {
    // empty -> 0
    // COMMON_WALL = 1;
    // METAL_WALL = 2;
    // TREE = 3;
    // RIVER = 4;
    // PLAYER1 = 5;
    // PLAYER2 = 6;
    // ENEMY = 7;
    private final int[][] mapGrid = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 0, 0, 0, 0},
            {0, 0, 4, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 7, 7, 7, 0, 0, 0, 0, 0, 7, 7, 7, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 7, 7, 7, 0, 0, 0, 0, 0, 7, 7, 7, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public void createMap(Pane gamePane, List<Wall> walls, List<Tank> enemyTanks) {
        int cellSize = 40;

        for (int row = 0; row < mapGrid.length; row++) {
            for (int col = 0; col < mapGrid[row].length; col++) {
                int cell = mapGrid[row][col];
                double x = col * cellSize;
                double y = row * cellSize;

                switch (cell) {
                    case 1 -> walls.add(GameObjectFactory.createCommonWall(x, y)); // Common Wall
                    case 2 -> walls.add(GameObjectFactory.createMetalWall(x, y));  // Metal Wall
                    case 3 -> gamePane.getChildren().add(GameObjectFactory.createTree(x, y).getImageView()); // Tree
                    case 4 -> gamePane.getChildren().add(GameObjectFactory.createRiver(x, y).getImageView()); // River
                    case 5 -> {
                        Tank playerTank = GameObjectFactory.createTank("player", x, y);
                        gamePane.getChildren().add(playerTank.getImageView()); // Player Tank
                    }
                    case 7 -> {
                        Tank enemyTank = GameObjectFactory.createTank("enemy", x, y);
                        enemyTank.setSpeed(2);
                        enemyTanks.add(enemyTank); // Enemy Tank
                        gamePane.getChildren().add(enemyTank.getImageView());
                    }
                }
            }
        }

        // Add walls to the gamePane
        for (Wall wall : walls) {
            gamePane.getChildren().add(wall.getImageView());
        }
    }
}
