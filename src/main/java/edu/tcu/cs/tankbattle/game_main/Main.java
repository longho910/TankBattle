package edu.tcu.cs.tankbattle.game_main;

import edu.tcu.cs.tankbattle.game_elements.Direction;
import edu.tcu.cs.tankbattle.game_elements.Tank;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create the main game container
        Pane gamePane = new Pane();
        Scene scene = new Scene(gamePane, 800, 600);

        // Initialize the game controller
        GameController controller = new GameController(gamePane);
        controller.startGameLoop(); // Start the game loop

        // Handle player keyboard input
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> {
                    controller.getPlayerTank().setDirection(Direction.UP);
                    controller.getPlayerTank().move(0, -1); // Move up
                }
                case DOWN -> {
                    controller.getPlayerTank().setDirection(Direction.DOWN);
                    controller.getPlayerTank().move(0, 1); // Move down
                }
                case LEFT -> {
                    controller.getPlayerTank().setDirection(Direction.LEFT);
                    controller.getPlayerTank().move(-1, 0); // Move left
                }
                case RIGHT -> {
                    controller.getPlayerTank().setDirection(Direction.RIGHT);
                    controller.getPlayerTank().move(1, 0); // Move right
                }
                case SPACE -> {
                    Tank playerTank = controller.getPlayerTank();
                    Direction direction = playerTank.getDirection();
                    double startX = playerTank.getImageView().getX() + 20;
                    double startY = playerTank.getImageView().getY() + 20;
                    controller.shootBullet(startX, startY, direction); // Fire a bullet
                }
            }
        });


        // Set up the primary stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tank Battle Game");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
