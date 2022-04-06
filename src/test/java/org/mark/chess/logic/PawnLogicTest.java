package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.enums.Color;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Pawn;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

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
    void testIsValidMove_WhenNullValues_ThenReturnFalse() {
        assertFalse(pawnLogic.isValidMove(null, null, null, null, false));
    }

    @Test
    void testIsValidMove_WhenInCheck_ThenReturnFalse() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().setCoordinates(VALID_MOVE_COORDINATES_TO);
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(true).when(pawnLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        assertFalse(pawnLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    void testIsValidMove_WhenJumping_ThenReturnFalse() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().setCoordinates(VALID_MOVE_COORDINATES_TO);
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(pawnLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);
        Mockito.doReturn(true).when(pawnLogic).isJumping(grid, from, to);

        assertFalse(pawnLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    void testIsValidMove_WhenFriendlyFire_ThenReturnFalse() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().setCoordinates(VALID_MOVE_COORDINATES_TO);
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(pawnLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);
        Mockito.doReturn(true).when(pawnLogic).isFriendlyFire(from.getPiece(), to);

        assertFalse(pawnLogic.isValidMove(grid, from, to, opponentFactory, false));
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
            "4;4;5;3;false",
            "4;4;3;2;false",
            "4;4;4;2;false",
            "4;4;5;2;false"}, delimiter = ';')
    void testIsValidMove_BasicMovesWhite(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(pawnLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "4;6;3;7;false",
            "4;6;4;7;false",
            "4;6;5;7;false",
            "4;6;3;6;false",
            "4;6;4;6;false",
            "4;6;5;6;false",
            "4;6;3;5;false",
            "4;6;4;5;true",
            "4;6;5;5;false",
            "4;6;3;4;false",
            "4;6;4;4;true",
            "4;6;5;4;false"}, delimiter = ';')
    void testIsValidMove_BasicLineMovesWhite(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(pawnLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "4;4;3;3;false",
            "4;4;4;3;false",
            "4;4;5;3;false",
            "4;4;3;4;false",
            "4;4;4;4;false",
            "4;4;5;4;false",
            "4;4;3;5;false",
            "4;4;4;5;true",
            "4;4;5;5;false",
            "4;4;3;6;false",
            "4;4;4;6;false",
            "4;4;5;6;false"}, delimiter = ';')
    void testIsValidMove_BasicMovesBlack(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(pawnLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "4;1;3;0;false",
            "4;1;4;0;false",
            "4;1;5;0;false",
            "4;1;3;1;false",
            "4;1;4;1;false",
            "4;1;5;1;false",
            "4;1;3;2;false",
            "4;1;4;2;true",
            "4;1;5;2;false",
            "4;1;3;3;false",
            "4;1;4;3;true",
            "4;1;5;3;false"}, delimiter = ';')
    void testIsValidMove_BasicLineMovesBlack(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(pawnLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

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
            "4;4;5;3;true",
            "4;4;3;2;false",
            "4;4;4;2;false",
            "4;4;5;2;false"}, delimiter = ';')
    void testIsValidMove_WhenWhiteCapturingBlack_ThenReturnTrue(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY)).setPiece(new Pawn().setColor(Color.BLACK));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(pawnLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "4;4;3;3;false",
            "4;4;4;3;false",
            "4;4;5;3;false",
            "4;4;3;4;false",
            "4;4;4;4;false",
            "4;4;5;4;false",
            "4;4;3;5;true",
            "4;4;4;5;false",
            "4;4;5;5;true",
            "4;4;3;6;false",
            "4;4;4;6;false",
            "4;4;5;6;false"}, delimiter = ';')
    void testIsValidMove_WhenBlackCapturingWhite_ThenReturnTrue(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY)).setPiece(new Pawn().setColor(Color.WHITE));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(pawnLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "3;5;false",
            "4;5;false",
            "5;5;false",
            "3;4;false",
            "4;4;false",
            "5;4;false",
            "3;3;true",
            "4;3;true",
            "5;3;true",
            "3;2;false",
            "4;2;false",
            "5;2;false"}, delimiter = ';')
    void testIsValidMove_WhenWhiteCapturingBlackEnPassant_ThenReturnTrue(int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(4, 4));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        List<Field> grid = new ArrayList<>();
        grid.add(new Field().setPiece(new Pawn().setMayBeCapturedEnPassant(true).setColor(Color.BLACK)).setCoordinates(new Coordinates(3, 4)));
        grid.add(new Field().setPiece(new Pawn().setMayBeCapturedEnPassant(true).setColor(Color.BLACK)).setCoordinates(new Coordinates(5, 4)));

        Mockito.doReturn(false).when(pawnLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "3;3;false",
            "4;3;false",
            "5;3;false",
            "3;4;false",
            "4;4;false",
            "5;4;false",
            "3;5;true",
            "4;5;true",
            "5;5;true",
            "3;6;false",
            "4;6;false",
            "5;6;false"}, delimiter = ';')
    void testIsValidMove_WhenBlackCapturingWhiteEnPassant_ThenReturnTrue(int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(4, 4));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        List<Field> grid = new ArrayList<>();
        grid.add(new Field().setPiece(new Pawn().setMayBeCapturedEnPassant(true).setColor(Color.WHITE)).setCoordinates(new Coordinates(3, 4)));
        grid.add(new Field().setPiece(new Pawn().setMayBeCapturedEnPassant(true).setColor(Color.WHITE)).setCoordinates(new Coordinates(5, 4)));

        Mockito.doReturn(false).when(pawnLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        boolean actual = pawnLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @Test
    void testMayBeCapturedEnPassant_WhenBlackMovesNextToWhite_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(3, 1));
        Field to = new Field().setCoordinates(new Coordinates(3, 3));
        List<Field> grid = new ArrayList<>();
        grid.add(new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(4, 3)));

        Mockito.doReturn(true).when(pawnLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        assertTrue(pawnLogic.mayBeCapturedEnPassant(grid, from, to));
    }

    @Test
    void testMayBeCapturedEnPassant_WhenWhiteMovesNextToBlack_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(3, 6));
        Field to = new Field().setCoordinates(new Coordinates(3, 4));
        List<Field> grid = new ArrayList<>();
        grid.add(new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(4, 4)));

        Mockito.doReturn(true).when(pawnLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        assertTrue(pawnLogic.mayBeCapturedEnPassant(grid, from, to));
    }

    @Test
    void testIsPawnBeingPromoted_WhenPawnBeingPromoted_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setPawnBeingPromoted(true));
        Field to = new Field();

        assertTrue(pawnLogic.isPawnBeingPromoted(from, to));
    }

    @Test
    void testIsPawnBeingPromoted_WhenWhitePawnStartsAtTheLastRow_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(4, 0));
        Field to = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(6, 6));

        assertTrue(pawnLogic.isPawnBeingPromoted(from, to));
    }

    @Test
    void testIsPawnBeingPromoted_WhenWhitePawnEndsAtTheLastRow_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(6, 6));
        Field to = new Field().setPiece(new Pawn().setColor(Color.WHITE)).setCoordinates(new Coordinates(4, 0));

        assertTrue(pawnLogic.isPawnBeingPromoted(from, to));
    }

    @Test
    void testIsPawnBeingPromoted_WhenBlackPawnStartsAtTheLastRow_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(4, 7));
        Field to = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(6, 6));

        assertTrue(pawnLogic.isPawnBeingPromoted(from, to));
    }

    @Test
    void testIsPawnBeingPromoted_WhenBlackPawnEndsAtTheLastRow_ThenReturnTrue() {
        Field from = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(6, 6));
        Field to = new Field().setPiece(new Pawn().setColor(Color.BLACK)).setCoordinates(new Coordinates(4, 7));

        assertTrue(pawnLogic.isPawnBeingPromoted(from, to));
    }
}