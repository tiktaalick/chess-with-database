package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class KingLogicTest {
    private static final Coordinates VALID_MOVE_COORDINATES_FROM = new Coordinates(3, 3);
    private static final Coordinates VALID_MOVE_COORDINATES_TO = new Coordinates(3, 4);

    @Spy
    @InjectMocks
    private KingLogic kingLogic;

    @Mock
    private PieceLogicFactory opponentFactory;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private RookLogic rookLogic;

    @Mock
    private QueenLogic queenLogic;

    @Test
    public void testIsValidMove_WhenNullValues_ThenReturnFalse() {
        assertFalse(kingLogic.isValidMove(null, null, null, null, false));
    }

    @Test
    public void testIsValidMove_WhenInCheck_ThenReturnFalse() {
        Field from = new Field().piece(new King().color(Color.WHITE)).coordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().coordinates(VALID_MOVE_COORDINATES_TO);
        List<Field> grid = new ArrayList<>();
        Field opponentField = new Field().piece(new Queen().color(Color.BLACK)).coordinates(new Coordinates(4, 4));
        grid.add(opponentField);

        doReturn(queenLogic).when(opponentFactory).getLogic(PieceType.QUEEN);
        doReturn(true).when(queenLogic).isValidMove(grid, opponentField, to, opponentFactory, true);

        assertFalse(kingLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    public void testIsValidMove_WhenJumping_ThenReturnFalse() {
        Field from = new Field().piece(new King().color(Color.WHITE)).coordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().coordinates(VALID_MOVE_COORDINATES_TO);
        List<Field> grid = new ArrayList<>();

        doReturn(false).when(kingLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);
        doReturn(true).when(kingLogic).isJumping(grid, from, to);

        assertFalse(kingLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    public void testIsValidMove_WhenFriendlyFire_ThenReturnFalse() {
        Field from = new Field().piece(new King().color(Color.WHITE)).coordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().coordinates(VALID_MOVE_COORDINATES_TO);
        List<Field> grid = new ArrayList<>();

        doReturn(false).when(kingLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);
        doReturn(true).when(kingLogic).isFriendlyFire(from.piece(), to);

        assertFalse(kingLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @ParameterizedTest
    @CsvSource(value = {"3;3;2;4;true",
                        "3;3;3;4;true",
                        "3;3;4;4;true",
                        "3;3;2;3;true",
                        "3;3;3;3;false",
                        "3;3;4;3;true",
                        "3;3;2;2;true",
                        "3;3;3;2;true",
                        "3;3;4;2;true",
                        "3;3;2;5;false",
                        "3;3;5;3;false",
                        "3;3;1;2;false",}, delimiter = ';')
    public void testIsValidMove_BasicMoves(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().piece(new King().color(Color.WHITE)).coordinates(new Coordinates(fromX, fromY));
        Field to = new Field().coordinates(new Coordinates(toX, toY));
        List<Field> grid = new ArrayList<>();

        doReturn(false).when(kingLogic).isInCheck(grid, from, to, false, opponentFactory, gridLogic);

        boolean actual = kingLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @Test
    public void isValidCastling_CastlingLeft_Valid() {
        Field from = new Field().piece(new King().color(Color.WHITE)).coordinates(new Coordinates(4, 7));
        Field to = new Field().coordinates(new Coordinates(2, 7));

        Coordinates rookCoordinates = new Coordinates(0, 7);
        Field rookField = new Field().piece(new Rook().color(Color.WHITE)).coordinates(rookCoordinates);

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);

        assertTrue(kingLogic.isValidCastling(grid, from, to, KingLogic.LEFT, opponentFactory, false, false));
    }

    @Test
    public void isValidCastling_CastlingRight_Valid() {
        Field from = new Field().piece(new King().color(Color.WHITE)).coordinates(new Coordinates(4, 7));
        Field to = new Field().coordinates(new Coordinates(6, 7));

        Coordinates rookCoordinates = new Coordinates(7, 7);
        Field rookField = new Field().piece(new Rook().color(Color.WHITE)).coordinates(rookCoordinates);

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);

        assertTrue(kingLogic.isValidCastling(grid, from, to, KingLogic.RIGHT, opponentFactory, false, false));
    }

    @Test
    public void isValidCastling_KingHasAlreadyMoved_Invalid() {
        Field from = new Field()
                .piece(new King().hasMovedAtLeastOnce(true).color(Color.WHITE))
                .coordinates(new Coordinates(4, 7));
        Field to = new Field().coordinates(new Coordinates(6, 7));

        Coordinates rookCoordinates = new Coordinates(7, 7);
        Field rookField = new Field().piece(new Rook().color(Color.WHITE)).coordinates(rookCoordinates);

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);

        assertFalse(kingLogic.isValidCastling(grid, from, to, KingLogic.RIGHT, opponentFactory, false, false));
    }

    @Test
    public void isValidCastling_KingHasMovedDuringCastling_Valid() {
        Field from = new Field()
                .piece(new King().hasMovedAtLeastOnce(true).color(Color.WHITE))
                .coordinates(new Coordinates(4, 7));
        Field to = new Field().coordinates(new Coordinates(6, 7));

        Coordinates rookCoordinates = new Coordinates(7, 7);
        Field rookField = new Field().piece(new Rook().color(Color.WHITE)).coordinates(rookCoordinates);

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);

        assertTrue(kingLogic.isValidCastling(grid, from, to, KingLogic.RIGHT, opponentFactory, false, true));
    }

    @Test
    public void isValidCastling_RookHasAlreadyMoved_Invalid() {
        Field from = new Field().piece(new King().color(Color.WHITE)).coordinates(new Coordinates(4, 7));
        Field to = new Field().coordinates(new Coordinates(6, 7));

        Coordinates rookCoordinates = new Coordinates(7, 7);
        Field rookField = new Field()
                .piece(new Rook().hasMovedAtLeastOnce(true).color(Color.WHITE))
                .coordinates(rookCoordinates);

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);

        assertFalse(kingLogic.isValidCastling(grid, from, to, KingLogic.RIGHT, opponentFactory, false, false));
    }

    @Test
    public void isValidCastling_KingInCheck_Invalid() {
        Field from = new Field().piece(new King().color(Color.WHITE)).coordinates(new Coordinates(4, 7));
        Field to = new Field().coordinates(new Coordinates(6, 7));

        Coordinates rookCoordinates = new Coordinates(7, 7);
        Field rookField = new Field().piece(new Rook().color(Color.WHITE)).coordinates(rookCoordinates);
        Field opponentField = new Field().piece(new Rook().color(Color.BLACK)).coordinates(new Coordinates(4, 0));

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);
        grid.add(opponentField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);
        doReturn(rookLogic).when(opponentFactory).getLogic(PieceType.ROOK);
        doReturn(true).when(rookLogic).isValidMove(grid, opponentField, from, opponentFactory, true);

        assertFalse(kingLogic.isValidCastling(grid, from, to, KingLogic.RIGHT, opponentFactory, false, false));
    }

    @Test
    public void isValidCastling_IsOppoment_Invalid() {
        Field from = new Field().piece(new King().color(Color.WHITE)).coordinates(new Coordinates(4, 7));
        Field to = new Field().coordinates(new Coordinates(6, 7));

        Coordinates rookCoordinates = new Coordinates(7, 7);
        Field rookField = new Field().piece(new Rook().color(Color.WHITE)).coordinates(rookCoordinates);

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);

        assertFalse(kingLogic.isValidCastling(grid, from, to, KingLogic.RIGHT, opponentFactory, true, false));
    }
}