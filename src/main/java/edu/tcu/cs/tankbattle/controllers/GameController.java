package edu.tcu.cs.tankbattle.controllers;

import edu.tcu.cs.tankbattle.models.*;
import edu.tcu.cs.tankbattle.factories.GameObjectFactory;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private Pane gamePane;
    private Tank playerTank;
    private List<Tank> enemyTanks = new ArrayList<>();
    private List<Missile> missiles = new ArrayList<>();
    private List<Wall> walls = new ArrayList<>();
    private List<MedPack> medPacks = new ArrayList<>();

    public GameController(Pane gamePane) {
        this.gamePane = gamePane;
        initializeGame();
    }

    private void initializeGame() {
        playerTank = GameObjectFactory.createTank("player", 400, 500);
        gamePane.getChildren().add(playerTank.getImageView());

        for (int i = 0; i < 3; i++) {
            Tank enemyTank = GameObjectFactory.createTank("enemy", 100 + i * 200, 100);
            enemyTank.setSpeed(2); // Match player speed
            enemyTanks.add(enemyTank);
            gamePane.getChildren().add(enemyTank.getImageView());
        }

        // Add common walls
        for (int i = 0; i < 5; i++) {
            Wall commonWall = GameObjectFactory.createCommonWall(100 + i * 50, 300);
            walls.add(commonWall);
            gamePane.getChildren().add(commonWall.getImageView());
        }

        // Add metal walls
        for (int i = 0; i < 5; i++) {
            Wall metalWall = GameObjectFactory.createMetalWall(200 + i * 50, 400);
            walls.add(metalWall);
            gamePane.getChildren().add(metalWall.getImageView());
        }
    }



    private void updateBullets() {
        List<Missile> toRemove = new ArrayList<>();

        for (Missile missile : missiles) {
            missile.move(); // Move the missile

            // Remove missiles that are out of bounds
            if (missile.getImageView().getY() < 0 || missile.getImageView().getY() > 600 ||
                    missile.getImageView().getX() < 0 || missile.getImageView().getX() > 800) {
                toRemove.add(missile);
            }
        }

        missiles.removeAll(toRemove);
        gamePane.getChildren().removeAll(toRemove.stream().map(Missile::getImageView).toList());
    }


    public void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBullets(); // Update missiles
                updateEnemyMovement(); // Move enemy tanks
                checkCollisions(); // Check for collisions
                checkGameStatus(); // Check win/lose conditions
            }
        };
        gameLoop.start();
    }

    private void updateEnemyMovement() {
        for (Tank enemy : enemyTanks) {
            enemy.move(0, 0); // Movement is handled by AIMovement strategy
        }
    }


    private void checkCollisions() {
        List<Missile> toRemove = new ArrayList<>();

        for (Missile missile : missiles) {
            // Collision with enemy tanks
            if (missile.getType().equals("player")) {
                for (Tank enemy : enemyTanks) {
                    if (missile.getImageView().getBoundsInParent().intersects(enemy.getImageView().getBoundsInParent())) {
                        enemy.takeDamage(25); // Damage enemy
                        if (!enemy.isAlive()) {
                            Explosion explosion = new Explosion(
                                    enemy.getImageView().getX(), enemy.getImageView().getY()
                            );
                            gamePane.getChildren().add(explosion.getImageView());
                            explosion.play(); // Trigger explosion animation
                            gamePane.getChildren().remove(enemy.getImageView());
                        }
                        toRemove.add(missile); // Remove the missile
                    }
                }
            }

            // Collision with walls
            for (Wall wall : walls) {
                if (missile.getImageView().getBoundsInParent().intersects(wall.getImageView().getBoundsInParent())) {
                    if (wall.isDestroyable()) {
                        gamePane.getChildren().remove(wall.getImageView()); // Remove destroyable wall
                        walls.remove(wall);
                    }
                    toRemove.add(missile); // Remove missile
                    break;
                }
            }
        }



        missiles.removeAll(toRemove);
        gamePane.getChildren().removeAll(toRemove.stream().map(Missile::getImageView).toList());
    }


    private void checkGameStatus() {
        // Check if the player has won
        if (enemyTanks.isEmpty()) {
            System.out.println("You Win!");
            stopGame();
        }

        // Check if the player has lost
        if (!playerTank.isAlive()) {
            System.out.println("Game Over!");
            stopGame();
        }
    }

    // Stops the game loop
    private void stopGame() {
        AnimationTimer gameLoop = null; // Replace with a reference to your AnimationTimer instance
        if (gameLoop != null) {
            gameLoop.stop(); // Stop the game loop
        }
        System.exit(0); // Exit the game
    }

    public Tank getPlayerTank() {
        return playerTank;
    }

    public void shootBullet(double startX, double startY, Direction direction) {
        // Select the correct bullet image based on direction
        String bulletImagePath = switch (direction) {
            case UP -> "/images/bulletU.gif";
            case DOWN -> "/images/bulletD.gif";
            case LEFT -> "/images/bulletL.gif";
            case RIGHT -> "/images/bulletR.gif";
        };

        // Create a missile
        Missile missile = new Missile(bulletImagePath, "player", startX, startY, 10);

        // Set movement direction for the bullet
        switch (direction) {
            case UP -> missile.setMovement(0, -1);
            case DOWN -> missile.setMovement(0, 1);
            case LEFT -> missile.setMovement(-1, 0);
            case RIGHT -> missile.setMovement(1, 0);
        }

        // Add the missile to the game
        missiles.add(missile);
        gamePane.getChildren().add(missile.getImageView());
    }




}
