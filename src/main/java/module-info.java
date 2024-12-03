module edu.tcu.cs.tankbattle {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.tcu.cs.tankbattle to javafx.fxml;
    exports edu.tcu.cs.tankbattle;
    exports edu.tcu.cs.tankbattle.game_main;
    opens edu.tcu.cs.tankbattle.game_main to javafx.fxml;
}