package main.ui;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Abstract class for all game stages
public abstract class GameStage {
    protected Stage stage;
    protected String title;
    protected BorderPane root;

    // Set up the stage - set title, width and height
    protected void setupStage() {
        stage = new Stage();
        stage.setTitle(title);
        root = new BorderPane();
        setupMenu();
        stage.setScene(new Scene(root, 500,500));
        stage.show();
    }

    // Set up the menu bar
    private void setupMenu() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newGameMenuItem = new MenuItem("New Game");
        MenuItem closeGameMenuItem = new MenuItem("Exit Game");
        fileMenu.getItems().add(newGameMenuItem);
        fileMenu.getItems().add(closeGameMenuItem);

        Menu helpMenu = new Menu("Help");
        MenuItem rulesMenuItem = new MenuItem("Rules");
        MenuItem moreInfoMenuItem = new MenuItem("More Info");
        helpMenu.getItems().add(rulesMenuItem);
        helpMenu.getItems().add(moreInfoMenuItem);

        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(helpMenu);

        VBox vBox = new VBox();
        vBox.getChildren().add(menuBar);
        root.setTop(vBox);
    }

    // Set up the game
    abstract protected void setupGame();

}
