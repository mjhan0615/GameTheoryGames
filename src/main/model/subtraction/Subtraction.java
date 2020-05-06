package main.model.subtraction;

import main.exceptions.InvalidMoveException;
import main.exceptions.InvalidStartNumberException;
import main.exceptions.InvalidSubtractionSetException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.sort;

// Creates a new Subtraction game
public class Subtraction {
    public final int MAX_START_NUM = 30;

    private int currentNumber;
    private List<Integer> subtractionSet;
    private boolean[] winningPositions;
    private boolean misere;
    private boolean playerTurn = true;

    // Creates a new Subtraction game with starting number 30, subtraction set {1, 2, 3}, and normal play
    public Subtraction() {
        currentNumber = MAX_START_NUM;
        subtractionSet = new ArrayList<>(Arrays.asList(1, 2, 3));
        misere = false;
        setWinningPositions();
    }

    // Creates a new Subtraction game with given starting number and subtraction set {1, ... , n}
    // Throws invalidNumber
    public Subtraction(int startNumber, int n, boolean isMisere) throws InvalidStartNumberException,
            InvalidSubtractionSetException {
        if (n < 1) {
            throw new InvalidSubtractionSetException();
        }
        if (startNumber < 1 || startNumber > MAX_START_NUM) {
            throw new InvalidStartNumberException();
        }

        currentNumber = startNumber;
        subtractionSet = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            subtractionSet.add(i);
        }
        misere = isMisere;
        setWinningPositions();
    }

    // Creates a new Subtraction game with given starting number and given subtraction set
    public Subtraction(int startNumber, List<Integer> set, boolean isMisere) throws InvalidStartNumberException,
            InvalidSubtractionSetException {

        if (startNumber < 1 || startNumber > MAX_START_NUM) {
            throw new InvalidStartNumberException();
        }
        currentNumber = startNumber;

        subtractionSet = new ArrayList<>();
        for (Integer num : set) {
            if (num < 1) {
                throw new InvalidSubtractionSetException();
            }
            subtractionSet.add(num);
        }
        sort(subtractionSet);

        misere = isMisere;
        setWinningPositions();
    }

    // Determines the winning (N) and losing (P) positions for this Subtraction game
    private void setWinningPositions() {
        winningPositions = new boolean[currentNumber + 1];
        if (misere) {
            winningPositions[0] = true;
        }
        for (int i = 0; i <= currentNumber; i++) {
            List<Integer> moves = getAvailableMoves(i);
            for (Integer num : moves) {
                // A position is an N position if any one follower is a P position.
                // A position is a P position if all followers are N positions.
                if (!winningPositions[i - num]) {
                    winningPositions[i] = true;
                    break;
                }
            }
        }
    }

    // Returns available moves at a given number
    private List<Integer> getAvailableMoves(int position) {
        List<Integer> result = new ArrayList<>();
        for (Integer num : subtractionSet) {
            if (position - num >= 0) {
                result.add(num);
            }
        }
        return result;
    }

    // Returns the available moves based on current position
    public List<Integer> getAvailableMoves() {
        return getAvailableMoves(currentNumber);
    }

    // Player makes move, subtracting the value from the current number
    // returns true if the move is a Winning move
    public void makeMove(int value) throws InvalidMoveException {
        if (!getAvailableMoves().contains(value)) {
            throw new InvalidMoveException();
        }
        currentNumber -= value;
        playerTurn = !playerTurn;
    }

    // Computer makes smallest optimal move. Returns the move made.
    public int makeMove() {
        List<Integer> moves = getAvailableMoves();
        playerTurn = !playerTurn;
        for (int move : moves) {
            if (!winningPositions[currentNumber - move]) {
                currentNumber -= move;
                return move;
            }
        }
        int move = getAvailableMoves().get(0);
        currentNumber -= move;
        return move;
    }

    // Checks whether game is over.
    public boolean isGameOver() {
        if (getAvailableMoves().size() == 0 || currentNumber == 0) {
            return true;
        }
        return false;
    }

    // Gets current position in game.
    public int getCurrentNumber() {
        return currentNumber;
    }

    // Returns true if it is currently the player's turn.
    public boolean isPlayerTurn() {
        return playerTurn;
    }

}
