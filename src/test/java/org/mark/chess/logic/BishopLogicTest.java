package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.enums.Color;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Bishop;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BishopLogicTest {
    private static final Coordinates VALID_MOVE_COORDINATES_FROM = new Coordinates(3, 3);
    private static final Coordinates VALID_MOVE_COORDINATES_TO   = new Coordinates(5, 5);

    @Spy
    @InjectMocks
    private BishopLogic bishopLogic;

    @Mock
    private PieceLogicFactory opponentFactory;

    @Mock
    private GridLogic gridLogic;

    @ParameterizedTest
    @CsvSource(value = {
            "3;3;3;3;false",
            "3;3;3;4;false",
            "3;3;4;4;true",
            "3;3;3;2;false",
            "3;3;2;2;true",
            "3;3;4;2;true",
            "3;3;2;4;true",
            "3;3;6;0;true",
            "3;3;1;5;true"}, delimiter = ';')
    void testIsValidMove_BasicMoves(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new Bishop().setColor(Color.WHITE)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(bishopLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory, gridLogic);

        boolean actual = bishopLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @Test
    void testIsValidMove_WhenFriendlyFire_ThenReturnFalse() {
        Field from = new Field().setPiece(new Bishop().setColor(Color.WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().setCoordinates(VALID_MOVE_COORDINATES_TO);
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(bishopLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory, gridLogic);
        Mockito.doReturn(true).when(bishopLogic).isFriendlyFire(from.getPiece(), to);

        assertFalse(bishopLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    void testIsValidMove_WhenInCheck_ThenReturnFalse() {
        Field from = new Field().setPiece(new Bishop().setColor(Color.WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().setCoordinates(VALID_MOVE_COORDINATES_TO);
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(true).when(bishopLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory, gridLogic);

        assertFalse(bishopLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    void testIsValidMove_WhenJumping_ThenReturnFalse() {
        Field from = new Field().setPiece(new Bishop().setColor(Color.WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().setCoordinates(VALID_MOVE_COORDINATES_TO);
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(bishopLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory, gridLogic);
        Mockito.doReturn(true).when(bishopLogic).isJumping(grid, from, to);

        assertFalse(bishopLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    void testIsValidMove_WhenNullValues_ThenReturnFalse() {
        assertFalse(bishopLogic.isValidMove(null, null, null, null, false));
    }
}