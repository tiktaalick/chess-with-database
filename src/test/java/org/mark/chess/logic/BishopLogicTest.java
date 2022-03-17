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
public class BishopLogicTest {
    @Spy
    @InjectMocks
    private BishopLogic bishopLogic;

    @Mock
    private PieceLogicFactory opponentFactory;

    @Mock
    private GridLogic gridLogic;

    @Test
    public void testIsValidMove_WhenNullValues_ThenReturnFalse() {
        assertFalse(bishopLogic.isValidMove(null, null, null, null, false));
    }

    @Test
    public void testIsValidMove_WhenInCheck_ThenReturnFalse() {
        Field from = new Field().piece(new Bishop().color(Color.WHITE)).coordinates(new Coordinates(3, 3));
        Field to = new Field().coordinates(new Coordinates(5, 5));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(true).when(bishopLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        assertFalse(bishopLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    public void testIsValidMove_WhenJumping_ThenReturnFalse() {
        Field from = new Field().piece(new Bishop().color(Color.WHITE)).coordinates(new Coordinates(3, 3));
        Field to = new Field().coordinates(new Coordinates(5, 5));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(bishopLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);
        Mockito.doReturn(true).when(bishopLogic).isJumping(grid, from, to);

        assertFalse(bishopLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    public void testIsValidMove_WhenFriendlyFire_ThenReturnFalse() {
        Field from = new Field().piece(new Bishop().color(Color.WHITE)).coordinates(new Coordinates(3, 3));
        Field to = new Field().coordinates(new Coordinates(5, 5));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(bishopLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);
        Mockito.doReturn(true).when(bishopLogic).isFriendlyFire(from.piece(), to);

        assertFalse(bishopLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @ParameterizedTest
    @CsvSource(value = {"3;3;3;3;false",
                        "3;3;3;4;false",
                        "3;3;4;4;true",
                        "3;3;3;2;false",
                        "3;3;2;2;true",
                        "3;3;4;2;true",
                        "3;3;2;4;true",
                        "3;3;6;0;true",
                        "3;3;1;5;true"}, delimiter = ';')
    public void testIsValidMove_BasicMoves(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().piece(new Bishop().color(Color.WHITE)).coordinates(new Coordinates(fromX, fromY));
        Field to = new Field().coordinates(new Coordinates(toX, toY));
        List<Field> grid = new ArrayList<>();

        Mockito.doReturn(false).when(bishopLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        boolean actual = bishopLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }
}