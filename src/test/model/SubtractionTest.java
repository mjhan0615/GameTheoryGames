package test.model;
import main.exceptions.InvalidMoveException;
import main.exceptions.InvalidStartNumberException;
import main.exceptions.InvalidSubtractionSetException;
import main.model.subtraction.Subtraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubtractionTest {
    Subtraction game;

    @BeforeEach
    public void setup() {
        game = new Subtraction();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(Arrays.asList(1, 2, 3), game.getAvailableMoves());
        assertEquals(30, game.getCurrentNumber());
    }

    @Test
    public void testTwoIntConstructorInvalidStartNum() {
        try {
            new Subtraction(-1, 4, true);
            fail("Expected InvalidStartNumberException");
        } catch (InvalidStartNumberException e) {
            // expected
        } catch (InvalidSubtractionSetException e) {
            fail("Expected InvalidStartNumberException");
        }
    }

    @Test
    public void testTwoIntConstructorInvalidSubtractionSet() {
        try {
            new Subtraction(10, 0, true);
            fail("Expected InvalidSubtractionSetException");
        } catch (InvalidStartNumberException e) {
            fail("Expected InvalidSubtractionSetException");
        } catch (InvalidSubtractionSetException e) {
            // expected
        }
    }

    @Test
    public void testTwoIntConstructor() {
        try {
            Subtraction gameTwo = new Subtraction(10, 2, true);
            assertEquals(10, gameTwo.getCurrentNumber());
            assertEquals(Arrays.asList(1,2), gameTwo.getAvailableMoves());
        } catch (InvalidStartNumberException | InvalidSubtractionSetException e) {
            fail("Did not expect any exception");
        }
    }

    @Test
    public void testCustomConstructorInvalidStartNum() {
        List<Integer> subtractionSet = new ArrayList<>(Arrays.asList(1,4));
        try {
            new Subtraction(0, subtractionSet, true);
            fail("Expected InvalidStartNumberException");
        } catch (InvalidStartNumberException e) {
            // expected
        } catch (InvalidSubtractionSetException e) {
            fail("Expected InvalidStartNumberException");
        }
    }

    @Test
    public void testCustomConstructorInvalidSubtractionSet() {
        List<Integer> subtractionSet = new ArrayList<>(Arrays.asList(0,4));
        try {
            new Subtraction(10, subtractionSet, true);
            fail("Expected InvalidSubtractionSetException");
        } catch (InvalidStartNumberException e) {
            fail("Expected InvalidSubtractionSetException");
        } catch (InvalidSubtractionSetException e) {
            // expected
        }
    }

    @Test
    public void testCustomConstructor() {
        List<Integer> subtractionSet = new ArrayList<>(Arrays.asList(4,1));
        try {
            Subtraction gameTwo = new Subtraction(10, subtractionSet, true);
            assertEquals(10, gameTwo.getCurrentNumber());
            assertEquals(Arrays.asList(1,4), gameTwo.getAvailableMoves());
        } catch (InvalidStartNumberException | InvalidSubtractionSetException e) {
            fail("Did not expect any exception");
        }
    }

    @Test
    public void testMakeMoveInvalid() {
        try {
            game.makeMove(4);
            fail("InvalidMoveException expected");
        } catch (InvalidMoveException e) {
            // Exception expected
        }
    }

    @Test
    public void testMakeMoveValid() {
        try {
            game.makeMove(3);
        } catch (InvalidMoveException e) {
            fail("InvalidMoveException not expected");
        }
        assertEquals(27, game.getCurrentNumber());
    }

    @Test
    public void testGetAvailableMovesFewLeft() {
        try {
            for (int i = 0; i < 30/3 -1; i++) {
                game.makeMove(3);
            }
            game.makeMove(1);
        } catch (InvalidMoveException e) {
            fail("InvalidMoveException not expected");
        }
        assertEquals(2, game.getCurrentNumber());
        assertEquals(Arrays.asList(1,2), game.getAvailableMoves());
        assertFalse(game.isGameOver());
    }

    @Test
    public void testGameOverTrue() {
        try {
            for (int i = 0; i < 30/3; i++) {
                game.makeMove(3);
            }
        } catch (InvalidMoveException e) {
            fail("InvalidMoveException not expected");
        }
        assertEquals(0, game.getCurrentNumber());
        assertEquals(new ArrayList<Integer>(), game.getAvailableMoves());
        assertTrue(game.isGameOver());
    }

    @Test
    public void testComputerMoveOpt() {
        int optMove = game.makeMove();
        assertEquals( 2, optMove);
        assertEquals(28, game.getCurrentNumber());
    }

    @Test
    public void testComputerMoveNoOpt() {
        game.makeMove();
        int move = game.makeMove();
        assertTrue(1 <= move && move <= 3);
        assertEquals(28-move, game.getCurrentNumber());
    }

}
