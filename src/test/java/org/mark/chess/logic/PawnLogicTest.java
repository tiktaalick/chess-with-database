package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.enums.Color;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Pawn;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PawnLogicTest {
    private static final Coordinates VALID_MOVE_COORDINATES_FROM = new Coordinates(3, 3);
    private static final Coordinates VALID_MOVE_COORDINATES_TO   = new Coordinates(3, 2);

    @Spy
    @InjectMocks
    private PawnLogic pawnLogic;

    @Mock
    private PieceLogicFactory opponentFactory;

    @Mock
    private GridLogic gridLogic;

    @Test
    void testIsPawnBeingPromoted_WhenBlackPawnEndsAtTheLastRow_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(6, 6));
        Field to = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(4, 1));

        assertTrue(pawnLogic.isPawnBeingPromoted(from, to));
    }

    @Test
    void testIsPawnBeingPromoted_WhenBlackPawnStartsAtTheLastRow_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(4, 1));
        Field to = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(6, 6));

        assertTrue(pawnLogic.isPawnBeingPromoted(from, to));
    }

    @Test
    void testIsPawnBeingPromoted_WhenPawnBeingPromoted_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setPawnBeingPromoted(true));
        Field to = new Field();

        assertTrue(pawnLogic.isPawnBeingPromoted(from, to));
    }

    @Test
    void testIsPawnBeingPromoted_WhenWhitePawnEndsAtTheLastRow_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(6, 6));
        Field to = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(4, 8));

        assertTrue(pawnLogic.isPawnBeingPromoted(from, to));
    }

    @Test
    void testIsPawnBeingPromoted_WhenWhitePawnStartsAtTheLastRow_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(4, 8));
        Field to = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(6, 6));

        assertTrue(pawnLogic.isPawnBeingPromoted(from, to));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "4;7;3;8;false",
            "4;7;4;8;false",
            "4;7;5;8;false",
            "4;7;3;7;false",
            "4;7;4;7;false",
            "4;7;5;7;false",
            "4;7;3;6;false",
            "4;7;4;6;true",
            "4;7;5;6;false",
            "4;7;3;5;false",
            "4;7;4;5;true",
            "4;7;5;5;false"}, delimiter = ';')
    void testIsValidMove_BasicLineMovesBlack(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        Grid grid = new Grid(new ArrayList<>());

        Mockito.doReturn(false).when(pawnLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "5;2;4;4;false",
            "5;2;5;4;true",
            "5;2;6;4;false",
            "5;2;4;3;false",
            "5;2;5;3;true",
            "5;2;6;3;false",
            "5;2;4;2;false",
            "5;2;5;2;false",
            "5;2;6;2;false",
            "5;2;4;1;false",
            "5;2;5;1;false",
            "5;2;6;1;false"}, delimiter = ';')
    void testIsValidMove_BasicLineMovesWhite(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        Grid grid = new Grid(new ArrayList<>());

        Mockito.doReturn(false).when(pawnLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "4;4;3;5;false",
            "4;4;4;5;false",
            "4;4;5;5;false",
            "4;4;3;4;false",
            "4;4;4;4;false",
            "4;4;5;4;false",
            "4;4;3;3;false",
            "4;4;4;3;true",
            "4;4;5;3;false"}, delimiter = ';')
    void testIsValidMove_BasicMovesBlack(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        Grid grid = new Grid(new ArrayList<>());

        Mockito.doReturn(false).when(pawnLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "4;4;3;5;false",
            "4;4;4;5;true",
            "4;4;5;5;false",
            "4;4;3;4;false",
            "4;4;4;4;false",
            "4;4;5;4;false",
            "4;4;3;3;false",
            "4;4;4;3;false",
            "4;4;5;3;false",
            "4;4;3;2;false",
            "4;4;4;2;false",
            "4;4;5;2;false"}, delimiter = ';')
    void testIsValidMove_BasicMovesWhite(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        Grid grid = new Grid(new ArrayList<>());

        Mockito.doReturn(false).when(pawnLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "3;3;true", "4;3;true", "5;3;true", "3;4;false", "4;4;false", "5;4;false", "3;5;false", "4;5;false", "5;5;false"}, delimiter = ';')
    void testIsValidMove_WhenBlackCapturesWhiteEnPassant_ThenReturnTrue(int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(4, 4));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        Grid grid = new Grid(Arrays.asList(new Field()
                        .setPiece(new Pawn().setMayBeCapturedEnPassant(true).setColor(Color.WHITE))
                        .setCoordinates(new Coordinates(3, 4)),
                new Field().setPiece(new Pawn().setMayBeCapturedEnPassant(true).setColor(Color.WHITE)).setCoordinates(new Coordinates(5, 4))));

        Mockito.doReturn(false).when(pawnLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "4;4;3;5;false",
            "4;4;4;5;false",
            "4;4;5;5;false",
            "4;4;3;4;false",
            "4;4;4;4;false",
            "4;4;5;4;false",
            "4;4;3;3;true",
            "4;4;4;3;false",
            "4;4;5;3;true"}, delimiter = ';')
    void testIsValidMove_WhenBlackCapturesWhite_ThenReturnTrue(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY)).setPiece(new Pawn().setColor(Color.WHITE));
        Grid grid = new Grid(new ArrayList<>());

        Mockito.doReturn(false).when(pawnLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @Test
    void testIsValidMove_WhenFriendlyFire_ThenReturnFalse() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().setCoordinates(VALID_MOVE_COORDINATES_TO);
        Grid grid = new Grid(new ArrayList<>());

        Mockito.doReturn(false).when(pawnLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory);
        Mockito.doReturn(true).when(pawnLogic).isFriendlyFire(from.getPiece(), to);

        assertFalse(pawnLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    void testIsValidMove_WhenInCheck_ThenReturnFalse() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().setCoordinates(VALID_MOVE_COORDINATES_TO);
        Grid grid = new Grid(new ArrayList<>());

        Mockito.doReturn(true).when(pawnLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory);

        assertFalse(pawnLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    void testIsValidMove_WhenJumping_ThenReturnFalse() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().setCoordinates(VALID_MOVE_COORDINATES_TO);
        Grid grid = new Grid(new ArrayList<>());

        Mockito.doReturn(false).when(pawnLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory);
        Mockito.doReturn(true).when(pawnLogic).isJumping(grid, from, to);

        assertFalse(pawnLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    void testIsValidMove_WhenNullValues_ThenReturnFalse() {
        assertFalse(pawnLogic.isValidMove(null, null, null, null, false));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "3;5;true", "4;5;true", "5;5;true", "3;4;false", "4;4;false", "5;4;false", "3;3;false", "4;3;false", "5;3;false"}, delimiter = ';')
    void testIsValidMove_WhenWhiteCapturesBlackEnPassant_ThenReturnTrue(int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(4, 4));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        Grid grid = new Grid(Arrays.asList(new Field()
                        .setPiece(new Pawn().setMayBeCapturedEnPassant(true).setColor(Color.BLACK))
                        .setCoordinates(new Coordinates(3, 4)),
                new Field().setPiece(new Pawn().setMayBeCapturedEnPassant(true).setColor(Color.BLACK)).setCoordinates(new Coordinates(5, 4))));

        Mockito.doReturn(false).when(pawnLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "4;4;3;5;true",
            "4;4;4;5;false",
            "4;4;5;5;true",
            "4;4;3;4;false",
            "4;4;4;4;false",
            "4;4;5;4;false",
            "4;4;3;3;false",
            "4;4;4;3;false",
            "4;4;5;3;false"}, delimiter = ';')
    void testIsValidMove_WhenWhiteCapturesBlack_ThenReturnTrue(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY)).setPiece(new Pawn().setColor(Color.BLACK));
        Grid grid = new Grid(new ArrayList<>());

        Mockito.doReturn(false).when(pawnLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @Test
    void testMayBeCapturedEnPassant_WhenBlackMovesNextToWhite_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(3, 7));
        Field to = new Field().setCoordinates(new Coordinates(3, 5));
        Grid grid = new Grid(Arrays.asList(new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(4, 5))));

        Mockito.doReturn(true).when(pawnLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory);

        assertTrue(pawnLogic.mayBeCapturedEnPassant(grid, from, to));
    }

    @Test
    void testMayBeCapturedEnPassant_WhenWhiteMovesNextToBlack_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(3, 2));
        Field to = new Field().setCoordinates(new Coordinates(3, 4));
        Grid grid = new Grid(Arrays.asList(new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(4, 4))));

        Mockito.doReturn(true).when(pawnLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory);

        assertTrue(pawnLogic.mayBeCapturedEnPassant(grid, from, to));
    }
}