package edu.tcu.cs.tankbattle.game_main;

import edu.tcu.cs.tankbattle.game_elements.*;
import edu.tcu.cs.tankbattle.strategies.AIMovement;
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
            AIMovement aiMovement = (AIMovement) enemy.getMovementStrategy(); // Ensure AIMovement is used
            double originalX = enemy.getImageView().getX();
            double originalY = enemy.getImageView().getY();

            // Execute movement using AIMovement
            aiMovement.move(enemy, 0, 0);

            // Check for collisions after movement
            boolean collides = walls.stream().anyMatch(wall ->
                    wall.getImageView().getBoundsInParent().intersects(
                            enemy.getImageView().getBoundsInParent()
                    ));

            // Revert movement if collision occurs
            if (collides) {
                enemy.getImageView().setX(originalX);
                enemy.getImageView().setY(originalY);
                // Change direction to avoid collision
                Direction newDirection = Direction.values()[(int) (Math.random() * Direction.values().length)];
                enemy.setDirection(newDirection);
            }

            // Randomly decide to shoot
            if (Math.random() < 0.02) { // 2% chance to shoot each frame
                shootBullet(enemy);
            }
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

            // Collision with player (enemy's missiles)
            if (missile.getType().equals("enemy")) {
                if (missile.getImageView().getBoundsInParent().intersects(playerTank.getImageView().getBoundsInParent())) {
                    playerTank.takeDamage(25);
                    toRemove.add(missile); // Remove missile
                    if (!playerTank.isAlive()) {
                        gamePane.getChildren().remove(playerTank.getImageView()); // Remove player tank from game pane
                        System.out.println("Game Over!");
                        stopGame();
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

    public void shootBullet(Tank tank) {
        // Get tank dimensions
        double tankWidth = tank.getImageView().getBoundsInParent().getWidth();
        double tankHeight = tank.getImageView().getBoundsInParent().getHeight();

        // Get tank position
        double tankX = tank.getImageView().getX();
        double tankY = tank.getImageView().getY();

        // Adjust bullet starting position based on tank center and direction
        double bulletStartX = tankX + tankWidth / 2 - 5; // Center X (subtracting 5 assumes bullet width is 10)
        double bulletStartY = tankY + tankHeight / 2 - 5; // Center Y (subtracting 5 assumes bullet height is 10)

        switch (tank.getDirection()) {
            case UP -> bulletStartY = tankY; // Top edge
            case DOWN -> bulletStartY = tankY + tankHeight; // Bottom edge
            case LEFT -> bulletStartX = tankX; // Left edge
            case RIGHT -> bulletStartX = tankX + tankWidth; // Right edge
        }

        // Select the correct bullet image based on direction
        String bulletImagePath = switch (tank.getDirection()) {
            case UP -> "/images/bulletU.gif";
            case DOWN -> "/images/bulletD.gif";
            case LEFT -> "/images/bulletL.gif";
            case RIGHT -> "/images/bulletR.gif";
        };

        // Determine missile type based on the tank type
        String missileType = tank == playerTank ? "player" : "enemy";

        // Create the missile
        Missile missile = new Missile(bulletImagePath, missileType, bulletStartX, bulletStartY, 10);

        // Set movement direction for the bullet
        switch (tank.getDirection()) {
            case UP -> missile.setMovement(0, -1); // Move up
            case DOWN -> missile.setMovement(0, 1); // Move down
            case LEFT -> missile.setMovement(-1, 0); // Move left
            case RIGHT -> missile.setMovement(1, 0); // Move right
        }

        // Add the missile to the game
        missiles.add(missile);
        gamePane.getChildren().add(missile.getImageView());
    }



    public List<Wall> getWall() {
        return walls;
    }
}
