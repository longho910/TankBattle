package edu.tcu.cs.tankbattle.game_main;

import edu.tcu.cs.tankbattle.game_elements.*;
import edu.tcu.cs.tankbattle.utils.GameObjectFactory;
import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
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
    private boolean gameStarted = false;


    public GameController(Pane gamePane) {
        this.gamePane = gamePane;
        initializeGame();
    }

    private void initializeGame() {
        // Set the background image
        ImageView background = new ImageView(new Image(getClass().getResource("/images/screen.jpg").toExternalForm()));
        background.setFitWidth(800); // Match game area width
        background.setFitHeight(600); // Match game area height
        gamePane.getChildren().add(background);

        // Initialize player tank
        playerTank = GameObjectFactory.createTank("player", 5, 5);
        gamePane.getChildren().add(playerTank.getImageView());

        // Initialization logic
        GameMap gameMap = new GameMap();
        gameMap.createMap(gamePane, walls, enemyTanks);

        // Mark the game as started only after initialization
        gameStarted = true;
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

    private void handleExplosion(double x, double y, ImageView target, boolean isEnemy) {
        Explosion explosion = new Explosion(x, y);
        gamePane.getChildren().add(explosion.getImageView());

        // Play explosion animation and remove the target and explosion after completion
        explosion.play(() -> {
            System.out.println("Explosion finished. Removing target and explosion."); // Debug
            gamePane.getChildren().remove(explosion.getImageView()); // Remove explosion
            gamePane.getChildren().remove(target); // Remove target (enemy or wall)
            if (isEnemy) {
                boolean removed = enemyTanks.removeIf(tank -> tank.getImageView() == target);
                System.out.println("Enemy removed: " + removed); // Debug
            } else {
                boolean removed = walls.removeIf(wall -> wall.getImageView() == target);
                System.out.println("Wall removed: " + removed); // Debug
            }
        });
    }


    private void checkCollisions() {
        List<Missile> toRemove = new ArrayList<>();

        for (Missile missile : missiles) {
            // Collision with enemy tanks
            if (missile.getType().equals("player")) {
                for (Tank enemy : enemyTanks) {
                    if (missile.getImageView().getBoundsInParent().intersects(enemy.getImageView().getBoundsInParent())) {
                        enemy.takeDamage(25);
                        if (!enemy.isAlive()) {
                            gamePane.getChildren().remove(enemy.getImageView()); // Remove enemy from game pane
                            enemyTanks.remove(enemy); // Remove enemy from list
                        }
                        toRemove.add(missile); // Remove missile
                        break; // Stop checking other enemies for this missile
                    }
                }
            }

            // Collision with walls
            for (Wall wall : walls) {
                if (missile.getImageView().getBoundsInParent().intersects(wall.getImageView().getBoundsInParent())) {
                    if (wall.isDestroyable()) {
                        gamePane.getChildren().remove(wall.getImageView()); // Remove wall from game pane
                        walls.remove(wall); // Remove wall from list
                    }
                    toRemove.add(missile); // Remove missile
                    break; // Stop checking other walls for this missile
                }
            }
        }

        // Remove collided missiles
        missiles.removeAll(toRemove);
        gamePane.getChildren().removeAll(toRemove.stream().map(Missile::getImageView).toList());
    }



    private void checkGameStatus() {
        if (!gameStarted) return; // Skip checking status until the game has started

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
