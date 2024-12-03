package edu.tcu.cs.tankbattle;

import edu.tcu.cs.tankbattle.game_elements.Direction;
import edu.tcu.cs.tankbattle.game_elements.Tank;
import edu.tcu.cs.tankbattle.game_main.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Pane gamePane = new Pane();
        Scene scene = new Scene(gamePane, 800, 600);

        GameController controller = new GameController(gamePane);
        controller.startGameLoop();

        scene.setOnKeyPressed(event -> {
            Tank playerTank = controller.getPlayerTank();
            switch (event.getCode()) {
                case UP -> {
                    playerTank.setDirection(Direction.UP);
                    playerTank.move(0, -1); // Move up
                }
                case DOWN -> {
                    playerTank.setDirection(Direction.DOWN);
                    playerTank.move(0, 1); // Move down
                }
                case LEFT -> {
                    playerTank.setDirection(Direction.LEFT);
                    playerTank.move(-1, 0); // Move left
                }
                case RIGHT -> {
                    playerTank.setDirection(Direction.RIGHT);
                    playerTank.move(1, 0); // Move right
                }
                case SPACE -> {
                    controller.shootBullet(
                            playerTank.getImageView().getX(),
                            playerTank.getImageView().getY(),
                            playerTank.getDirection()
                    );
                }
            }
        });


        primaryStage.setScene(scene);
        primaryStage.setTitle("Tank Battle Game");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
