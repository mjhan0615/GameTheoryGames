package main.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import main.exceptions.InvalidMoveException;
import main.exceptions.InvalidStartNumberException;
import main.exceptions.InvalidSubtractionSetException;
import main.model.subtraction.Subtraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Stage for Subtraction game
public class SubtractionStage extends GameStage {
    private Subtraction subtraction;
    private List<Rectangle> tiles;
    private VBox centerVBox;
    private VBox gameSettingsBox;
    private Label currentNumberLabel;
    private Label subtractionSetLabel;
    private Label playStyleLabel;

    public SubtractionStage() {
        setupStage();
        subtraction = new Subtraction();
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
        gameSettingsBox.setPadding(new Insets(10, 10, 10, 10));
        currentNumberLabel = new Label(numTilesLeft());
        subtractionSetLabel = new Label(subtractionSet());
        playStyleLabel = new Label(playStyle());
        currentNumberLabel.setFont(new Font("Arial", 15));
        subtractionSetLabel.setFont(new Font("Arial", 15));
        playStyleLabel.setFont(new Font("Arial", 15));
        gameSettingsBox.getChildren().add(currentNumberLabel);
        gameSettingsBox.getChildren().add(subtractionSetLabel);
        gameSettingsBox.getChildren().add(playStyleLabel);
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
                Label endLabel = subtraction.didPlayerWin() ? new Label("You won!") : new Label("You lost :P");
                Color labelColour = subtraction.didPlayerWin() ? Color.GREEN : Color.RED;
                endLabel.setFont(new Font("Arial", 30));;
                endLabel.setTextFill(labelColour);
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
            Label endLabel = subtraction.didPlayerWin() ? new Label("You won!") : new Label("You lost :P");
            Color labelColour = subtraction.didPlayerWin() ? Color.GREEN : Color.RED;
            endLabel.setFont(new Font("Arial", 30));
            endLabel.setTextFill(labelColour);
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

    // Returns the type of play as a String
    private String playStyle() {
        String playStyle;
        if (subtraction.isMisere()) {
            playStyle = "Misere play";
        } else {
            playStyle = "Normal play";
        }
        return "Play style: " + playStyle;
    }

    @Override
    protected void newGameSettings() {
        new SubtractionSettingsStage();
    }

    // Settings stage for subtraction game
    private class SubtractionSettingsStage extends SettingsStage {
        VBox settings;
        TextField startNumField;
        TextField subtractionSetField;
        RadioButton normalButton;
        RadioButton misereButton;
        Label messageLabel;

        public SubtractionSettingsStage() {
            title = "Subtraction Game Settings";
            super.setupSettingsStage();
        }

        @Override
        protected void setupSettingsArea() {
            settings = new VBox(10);
            settings.setAlignment(Pos.CENTER);

            setupStartNumberField();
            setupSubtractionSetField();
            setupPlayStyleRadioButtons();
            messageLabel = new Label("");
            messageLabel.setTextFill(Color.RED);
            settings.getChildren().add(messageLabel);

            root.setCenter(settings);
        }

        // Creates a text box to enter starting number of tiles
        private void setupStartNumberField() {
            HBox startNumBox = new HBox(5);
            startNumBox.setAlignment(Pos.CENTER);
            Label startNumLabel = new Label("Number of tiles: ");
            startNumField = new TextField("30");
            startNumField.setMaxWidth(100);
            startNumLabel.setLabelFor(startNumField);
            startNumBox.getChildren().add(startNumLabel);
            startNumBox.getChildren().add(startNumField);
            settings.getChildren().add(startNumBox);
        }

        // Creates a text box to enter subtraction set
        private void setupSubtractionSetField() {
            HBox subtractionSetBox = new HBox(5);
            subtractionSetBox.setAlignment(Pos.CENTER);
            Label subtractionSetLabel = new Label("Subtraction set: ");
            subtractionSetField = new TextField("1, 2, 3");
            subtractionSetField.setMaxWidth(100);
            subtractionSetLabel.setLabelFor(subtractionSetField);
            subtractionSetBox.getChildren().add(subtractionSetLabel);
            subtractionSetBox.getChildren().add(subtractionSetField);
            settings.getChildren().add(subtractionSetBox);
        }

        // Creates the radio buttons to select between normal and misere play
        private void setupPlayStyleRadioButtons() {
            ToggleGroup toggleGroup = new ToggleGroup();
            normalButton = new RadioButton("Normal Play");
            normalButton.setToggleGroup(toggleGroup);
            normalButton.setSelected(true);
            misereButton = new RadioButton("Misere Play");
            misereButton.setToggleGroup(toggleGroup);
            HBox radioButtons = new HBox(20);
            radioButtons.setAlignment(Pos.CENTER);
            radioButtons.getChildren().add(normalButton);
            radioButtons.getChildren().add(misereButton);
            settings.getChildren().add(radioButtons);
        }

        @Override
        protected void applyAndCreateNewGame() {
            int startNum;
            try {
                startNum = Integer.parseInt(startNumField.getText());
            } catch (NumberFormatException e){
                messageLabel.setText("Please enter a valid starting number between 0 and " + Subtraction.MAX_START_NUM + ".");
                return;
            }

            String subtractionFieldText = subtractionSetField.getText();
            List<String> subtractionSetStrings = Arrays.asList(subtractionFieldText.split("\\s*,\\s*"));
            List<Integer> subtractionSet = new ArrayList<>();
            for (String entry: subtractionSetStrings) {
                try {
                    subtractionSet.add(Integer.parseInt(entry));
                } catch (NumberFormatException e){
                    messageLabel.setText("Please enter comma-separated numbers only.");
                    return;
                }
            }

            boolean isMisere = misereButton.isSelected();

            try {
                subtraction = new Subtraction(startNum, subtractionSet, isMisere);
            } catch (InvalidStartNumberException e) {
                messageLabel.setText("The starting number must be between 0 and 30.");
                return;
            } catch (InvalidSubtractionSetException e) {
                messageLabel.setText("All numbers must be between 1 and the starting number.");
                return;
            }

            setupGame();
            settingsStage.close();
        }

    }

}
