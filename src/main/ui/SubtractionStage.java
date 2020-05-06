package main.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import main.exceptions.InvalidMoveException;
import main.model.subtraction.Subtraction;

import java.util.ArrayList;
import java.util.List;

// Stage for Subtraction game
public class SubtractionStage extends GameStage {
    Subtraction subtraction;
    List<Rectangle> tiles;
    VBox vBox;

    public SubtractionStage() {
        setupStage();
        setupGame();
    }

    // Sets up the stage
    protected void setupStage() {
        title = "Subtraction";
        super.setupStage();

    }

    // Sets up the game
    protected void setupGame() {
        subtraction = new Subtraction();
        tiles = new ArrayList<>();
        vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER);

        // Add tile for every number in the subtraction game
        for (int i = 0; i < subtraction.getCurrentNumber(); i++) {
            Rectangle tile = new Rectangle(50,10);
            tile.setFill(Color.BLUEVIOLET);
            vBox.getChildren().add(tile);
            tiles.add(tile);

            // Change colour of tile when hovered over
            tile.hoverProperty().addListener((obs, oldVal, newValue) -> {
                int tileIndex = tiles.indexOf(tile);
                if (newValue) {
                    if (tilesShouldRespond(tileIndex)) {
                        for (int j = 0; j <= tileIndex; j++) {
                            tiles.get(j).setFill(Color.VIOLET);
                        }
                    }
                } else {
                    for (int j = 0; j <= tileIndex; j++) {
                        tiles.get(j).setFill(Color.BLUEVIOLET);
                    }
                }
            });

            // Remove tiles on click
            tile.setOnMouseClicked(event -> {
                int tileIndex = tiles.indexOf(tile);
                if (tilesShouldRespond(tileIndex)) {
                    handlePlayerTurn(tileIndex);
                    if (!subtraction.isGameOver()) {
                        handleComputerMove();
                    }
                }
            });
        }

        root.setCenter(vBox);
    }

    // Handle computer turn
    private void handleComputerMove() {
        int move = subtraction.makeMove();

        // Add a fade out to the computer's moves
        ParallelTransition fadeOut = new ParallelTransition();
        for (int j = 0; j < move; j++) {
            FadeTransition tileFade = new FadeTransition();
            tileFade.setDuration(Duration.millis(1000));
            tileFade.setFromValue(1.0);
            tileFade.setToValue(0.0);
            tileFade.setNode(tiles.get(j));
            fadeOut.getChildren().add(tileFade);
        }
        tiles = tiles.subList(move, tiles.size());

        // Check if game over
        if (subtraction.isGameOver()) {
            fadeOut.setOnFinished(event -> {
                Label endLabel = new Label("You lost :P");
                vBox.getChildren().clear();
                vBox.getChildren().add(endLabel);
            });
        }

        fadeOut.play();
    }

    // Handle player turn
    private void handlePlayerTurn(int tileIndex) {
        try {
            subtraction.makeMove(tileIndex + 1);
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        // Remove playable tiles and set invisible
        for (int j = 0; j <= tileIndex; j++) {
            tiles.get(j).setVisible(false);
        }
        tiles = tiles.subList(tileIndex + 1, tiles.size());

        // Check if game over
        if (subtraction.isGameOver()) {
            Label endLabel = new Label("You won!");
            vBox.getChildren().clear();
            vBox.getChildren().add(endLabel);
        }
    }


    // Returns true if the tiles should respond to mouse and hover events
    private boolean tilesShouldRespond(int tileIndex) {
        return subtraction.getAvailableMoves().contains(tileIndex + 1) && subtraction.isPlayerTurn();
    }




}
