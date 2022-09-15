package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Rook;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mark.chess.enums.Color.WHITE;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RookLogicTest {
    public static final  char        DELIMITER                   = ';';
    private static final Coordinates VALID_MOVE_COORDINATES_FROM = new Coordinates(3, 3);
    private static final Coordinates VALID_MOVE_COORDINATES_TO   = new Coordinates(3, 2);

    @Spy
    @InjectMocks
    private RookLogic rookLogic;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private CheckLogic checkLogic;

    @ParameterizedTest
    @CsvSource(value = {
            "3;3;2;4;false",
            "3;3;3;4;true",
            "3;3;4;2;false",
            "3;3;2;3;true",
            "3;3;3;3;false",
            "3;3;4;3;true",
            "3;3;2;4;false",
            "3;3;3;4;true",
            "3;3;4;4;false",
            "3;3;0;6;false",
            "3;3;6;3;true",
            "3;3;3;6;true",
            "3;3;6;0;false"}, delimiter = DELIMITER)
    void testIsValidMove_BasicMoves(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field(new Rook(WHITE)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field(null).setCoordinates(new Coordinates(toX, toY));
        Grid grid = Grid.createGrid(new ArrayList<>(), gridLogic);

        Mockito.doReturn(false).when(checkLogic).isMovingIntoCheck(grid, from, to, false, gridLogic);

        boolean actual = rookLogic.isValidMove(grid, from, to, false);
        assertEquals(expected, actual);
    }

    @Test
    void testIsValidMove_WhenFriendlyFire_ThenReturnFalse() {
        Field from = new Field(new Rook(WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field(null).setCoordinates(VALID_MOVE_COORDINATES_TO);
        Grid grid = Grid.createGrid(new ArrayList<>(), gridLogic);

        Mockito.doReturn(false).when(checkLogic).isMovingIntoCheck(grid, from, to, false, gridLogic);
        Mockito.doReturn(true).when(rookLogic).isFriendlyFire(from.getPiece(), to);

        assertFalse(rookLogic.isValidMove(grid, from, to, false));
    }

    @Test
    void testIsValidMove_WhenInCheck_ThenReturnFalse() {
        Field from = new Field(new Rook(WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field(null).setCoordinates(VALID_MOVE_COORDINATES_TO);
        Grid grid = Grid.createGrid(new ArrayList<>(), gridLogic);

        Mockito.doReturn(true).when(checkLogic).isMovingIntoCheck(grid, from, to, false, gridLogic);

        assertFalse(rookLogic.isValidMove(grid, from, to, false));
    }

    @Test
    void testIsValidMove_WhenJumping_ThenReturnFalse() {
        Field from = new Field(new Rook(WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field(null).setCoordinates(VALID_MOVE_COORDINATES_TO);
        Grid grid = Grid.createGrid(new ArrayList<>(), gridLogic);

        Mockito.doReturn(false).when(checkLogic).isMovingIntoCheck(grid, from, to, false, gridLogic);
        Mockito.doReturn(true).when(rookLogic).isJumping(grid, from, to);

        assertFalse(rookLogic.isValidMove(grid, from, to, false));
    }

    @Test
    void testIsValidMove_WhenNullValues_ThenReturnFalse() {
        assertFalse(rookLogic.isValidMove(null, null, null, false));
    }
}