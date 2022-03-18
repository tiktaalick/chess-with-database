package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.enums.Color;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Queen;
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
class QueenLogicTest {
    @Spy
    @InjectMocks
    private QueenLogic queenLogic;

    @Mock
    private PieceLogicFactory opponentFactory;

    @Mock
    private GridLogic gridLogic;

    @Test
    public void testIsValidMove_WhenNullValues_ThenReturnFalse() {
        assertFalse(queenLogic.isValidMove(null, null, null, null, false));
    }

    @Test
    public void testIsValidMove_WhenInCheck_ThenReturnFalse() {
        Field from = new Field().piece(new Queen().color(Color.WHITE)).coordinates(new Coordinates(3, 3));
        Field to = new Field().coordinates(new Coordinates(5, 5));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(true).when(queenLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        assertFalse(queenLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    public void testIsValidMove_WhenJumping_ThenReturnFalse() {
        Field from = new Field().piece(new Queen().color(Color.WHITE)).coordinates(new Coordinates(3, 3));
        Field to = new Field().coordinates(new Coordinates(5, 5));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(queenLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);
        Mockito.doReturn(true).when(queenLogic).isJumping(grid, from, to);

        assertFalse(queenLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    public void testIsValidMove_WhenFriendlyFire_ThenReturnFalse() {
        Field from = new Field().piece(new Queen().color(Color.WHITE)).coordinates(new Coordinates(3, 3));
        Field to = new Field().coordinates(new Coordinates(5, 5));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(queenLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);
        Mockito.doReturn(true).when(queenLogic).isFriendlyFire(from.piece(), to);

        assertFalse(queenLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @ParameterizedTest
    @CsvSource(value = {"3;3;2;4;true",
                        "3;3;3;4;true",
                        "3;3;4;2;true",
                        "3;3;2;3;true",
                        "3;3;3;3;false",
                        "3;3;4;3;true",
                        "3;3;2;4;true",
                        "3;3;3;4;true",
                        "3;3;4;4;true",
                        "3;3;0;6;true",
                        "3;3;6;3;true",
                        "3;3;3;6;true",
                        "3;3;6;0;true"}, delimiter = ';')
    public void testIsValidMove_BasicMoves(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().piece(new Queen().color(Color.WHITE)).coordinates(new Coordinates(fromX, fromY));
        Field to = new Field().coordinates(new Coordinates(toX, toY));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(queenLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        boolean actual = queenLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }
}