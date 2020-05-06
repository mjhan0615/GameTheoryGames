package main.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

// Window to change game settings
public abstract class SettingsStage {
    protected Stage settingsStage;
    protected BorderPane root;
    protected String title;
    private HBox buttons;
    private Button OKButton;
    private Button cancelButton;

    protected void setupSettingsStage() {
        settingsStage = new Stage();
        settingsStage.setTitle(title);
        root = new BorderPane();

        initializeButtons();
        setupSettingsArea();

        settingsStage.setScene(new Scene(root, 400,250));
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.show();
    }

    // Create OK and Cancel buttons
    private void initializeButtons() {
        buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        OKButton = new Button("OK");
        cancelButton = new Button("Cancel");
        OKButton.setOnAction(event -> applyAndCreateNewGame());
        cancelButton.setOnAction(event -> settingsStage.close());
        buttons.getChildren().add(OKButton);
        buttons.getChildren().add(cancelButton);
        buttons.setPadding(new Insets(10, 10, 10, 10));
        root.setBottom(buttons);
    }

    protected abstract void setupSettingsArea();

    // Apply the selected settings and create a new game
    protected abstract void applyAndCreateNewGame();
}
