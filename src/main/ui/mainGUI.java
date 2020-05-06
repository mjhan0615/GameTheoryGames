package main.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// Entry point to GUI application
public class mainGUI extends Application {
    Button subtractionBtn;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Combinatorial Games");
        subtractionBtn = new Button();
        subtractionBtn.setText("Subtraction");
        subtractionBtn.setOnAction(event -> new SubtractionStage());

        StackPane root = new StackPane();
        root.getChildren().add(subtractionBtn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
