package main.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import main.exceptions.InvalidMoveException;
import main.model.subtraction.Subtraction;

import java.util.ArrayList;
import java.util.List;

// Stage for Subtraction game
public class SubtractionStage extends GameStage {
    Subtraction subtraction;
    List<Rectangle> tiles;
    VBox centerVBox;
    VBox gameSettingsBox;
    Label currentNumberLabel;
    Label subtractionSetLabel;

    public SubtractionStage() {
        setupStage();
        setupGame();
    }

    // Sets up the stage
    protected void setupStage() {
        title = "Subtraction";
        super.setupStage();

    }

    // Sets up the game area
    @Override
    protected void setupGame() {
        subtraction = new Subtraction();
        tiles = new ArrayList<>();
        centerVBox = new VBox(5);
        centerVBox.setAlignment(Pos.CENTER);

        // Add tile for every number in the subtraction game
        for (int i = 0; i < subtraction.getCurrentNumber(); i++) {
            Rectangle tile = new Rectangle(50,10);
            tile.setFill(Color.BLUEVIOLET);
            centerVBox.getChildren().add(tile);
            tiles.add(tile);
            handleHover(tile);
            handleClick(tile);

        }

        root.setCenter(centerVBox);
        initializeGameSettingsBox();
    }

    // Set up settings and current tile number area
    protected void initializeGameSettingsBox() {
        gameSettingsBox = new VBox(10);
        gameSettingsBox.setAlignment(Pos.CENTER);
        currentNumberLabel = new Label(numTilesLeft());
        subtractionSetLabel = new Label(subtractionSet());
        currentNumberLabel.setFont(new Font("Arial", 15));
        subtractionSetLabel.setFont(new Font("Arial", 15));
        gameSettingsBox.getChildren().add(currentNumberLabel);
        gameSettingsBox.getChildren().add(subtractionSetLabel);
        root.setRight(gameSettingsBox);
    }

    // Remove tiles on click if it is the player's turn
    private void handleClick(Rectangle tile) {
        tile.setOnMouseClicked(event -> {
            int tileIndex = tiles.indexOf(tile);
            if (tilesShouldRespond(tileIndex)) {
                playerTurn(tileIndex);
                if (!subtraction.isGameOver()) {
                    computerMove();
                }
            }
        });
    }

    // Change colour of tile when hovered over
    private void handleHover(Rectangle tile) {
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
    }

    // Handle computer turn
    private void computerMove() {
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

        fadeOut.setOnFinished(event -> {
            // Update the number of tiles left label
            currentNumberLabel.setText(numTilesLeft());
            if (subtraction.isGameOver()) {
                // Check if game over
                Label endLabel = new Label("You lost :P");
                endLabel.setFont(new Font("Arial", 30));;
                endLabel.setTextFill(Color.RED);
                centerVBox.getChildren().clear();
                centerVBox.getChildren().add(endLabel);
            }
        });

        fadeOut.play();
    }

    // Handle player turn
    private void playerTurn(int tileIndex) {
        try {
            subtraction.makeMove(tileIndex + 1);
            currentNumberLabel.setText(numTilesLeft());
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
            endLabel.setFont(new Font("Arial", 30));
            endLabel.setTextFill(Color.GREEN);
            centerVBox.getChildren().clear();
            centerVBox.getChildren().add(endLabel);
        }
    }

    // Returns true if the tiles should respond to mouse and hover events
    private boolean tilesShouldRespond(int tileIndex) {
        return subtraction.getAvailableMoves().contains(tileIndex + 1) && subtraction.isPlayerTurn();
    }

    // Returns the number of tiles remaining as a String
    private String numTilesLeft() {
        Integer numLeft = subtraction.getCurrentNumber();
        return "Number of tiles left: " + numLeft.toString();
    }

    // Returns the subtraction set as a String
    private String subtractionSet() {
        List<Integer> moves = subtraction.getAvailableMoves();
        String movesString = "";
        for (int i = 0; i < moves.size()-1; i++) {
            movesString += moves.get(i) + ", ";
        }
        movesString += moves.get(moves.size()-1);
        return "Subtraction set: " + movesString;
    }

    @Override
    protected void newGameSettings() {

    }

}
