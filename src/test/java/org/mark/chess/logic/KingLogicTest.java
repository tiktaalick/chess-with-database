package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.King;
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KingLogicTest {
    private static final Coordinates VALID_MOVE_COORDINATES_FROM = new Coordinates(3, 3);
    private static final Coordinates VALID_MOVE_COORDINATES_TO   = new Coordinates(3, 4);

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
    void testIsValidMove_WhenNullValues_ThenReturnFalse() {
        assertFalse(kingLogic.isValidMove(null, null, null, null, false));
    }

    @Test
    void testIsValidMove_WhenInCheck_ThenReturnFalse() {
        Field from = new Field().setPiece(new King().setColor(Color.WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().setCoordinates(VALID_MOVE_COORDINATES_TO);
        List<Field> grid = new ArrayList<>();
        Field opponentField = new Field().setPiece(new Queen().setColor(Color.BLACK)).setCoordinates(new Coordinates(4, 4));
        grid.add(opponentField);

        when(opponentFactory.getLogic(PieceType.QUEEN)).thenReturn(queenLogic);
        when(queenLogic.isValidMove(grid, opponentField, to, opponentFactory, true)).thenReturn(true);
        when(kingLogic.isMovingIntoCheck(grid, from, to, false, opponentFactory, gridLogic)).thenReturn(true);

        assertFalse(kingLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    void testIsValidMove_WhenJumping_ThenReturnFalse() {
        Field from = new Field().setPiece(new King().setColor(Color.WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().setCoordinates(VALID_MOVE_COORDINATES_TO);
        List<Field> grid = new ArrayList<>();

        doReturn(false).when(kingLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory, gridLogic);
        doReturn(true).when(kingLogic).isJumping(grid, from, to);

        assertFalse(kingLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @Test
    void testIsValidMove_WhenFriendlyFire_ThenReturnFalse() {
        Field from = new Field().setPiece(new King().setColor(Color.WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field().setCoordinates(VALID_MOVE_COORDINATES_TO);
        List<Field> grid = new ArrayList<>();

        doReturn(false).when(kingLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory, gridLogic);
        doReturn(true).when(kingLogic).isFriendlyFire(from.getPiece(), to);

        assertFalse(kingLogic.isValidMove(grid, from, to, opponentFactory, false));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "3;3;2;4;true",
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
    void testIsValidMove_BasicMoves(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field().setPiece(new King().setColor(Color.WHITE)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field().setCoordinates(new Coordinates(toX, toY));
        List<Field> grid = new ArrayList<>();

        doReturn(false).when(kingLogic).isMovingIntoCheck(grid, from, to, false, opponentFactory, gridLogic);

        boolean actual = kingLogic.isValidMove(grid, from, to, opponentFactory, false);
        assertEquals(expected, actual);
    }

    @Test
    void isValidCastling_CastlingLeft_Valid() {
        Field from = new Field().setPiece(new King().setColor(Color.WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field().setCoordinates(new Coordinates(3, 1));

        Coordinates rookCoordinates = new Coordinates(1, 1);
        Field rookField = new Field().setPiece(new Rook().setColor(Color.WHITE)).setCoordinates(rookCoordinates);

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);

        assertTrue(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_LEFT, opponentFactory, false, false));
    }

    @Test
    void isValidCastling_CastlingRight_Valid() {
        Field from = new Field().setPiece(new King().setColor(Color.WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field().setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field().setPiece(new Rook().setColor(Color.WHITE)).setCoordinates(rookCoordinates);

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);

        assertTrue(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_RIGHT, opponentFactory, false, false));
    }

    @Test
    void isValidCastling_KingHasAlreadyMoved_Invalid() {
        Field from = new Field().setPiece(new King().setHasMovedAtLeastOnce(true).setColor(Color.WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field().setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field().setPiece(new Rook().setColor(Color.WHITE)).setCoordinates(rookCoordinates);

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);

        assertFalse(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_RIGHT, opponentFactory, false, false));
    }

    @Test
    void isValidCastling_KingHasMovedDuringCastling_Valid() {
        Field from = new Field().setPiece(new King().setHasMovedAtLeastOnce(true).setColor(Color.WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field().setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field().setPiece(new Rook().setColor(Color.WHITE)).setCoordinates(rookCoordinates);

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);

        assertTrue(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_RIGHT, opponentFactory, false, true));
    }

    @Test
    void isValidCastling_RookHasAlreadyMoved_Invalid() {
        Field from = new Field().setPiece(new King().setColor(Color.WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field().setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field().setPiece(new Rook().setHasMovedAtLeastOnce(true).setColor(Color.WHITE)).setCoordinates(rookCoordinates);

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);

        assertFalse(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_RIGHT, opponentFactory, false, false));
    }

    @Test
    void isValidCastling_KingInCheck_Invalid() {
        Field from = new Field().setPiece(new King().setColor(Color.WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field().setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field().setPiece(new Rook().setColor(Color.WHITE)).setCoordinates(rookCoordinates);
        Field opponentField = new Field().setPiece(new Rook().setColor(Color.BLACK)).setCoordinates(new Coordinates(5, 8));

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);
        grid.add(opponentField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);
        doReturn(rookLogic).when(opponentFactory).getLogic(PieceType.ROOK);
        doReturn(true).when(rookLogic).isValidMove(grid, opponentField, from, opponentFactory, true);

        assertFalse(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_RIGHT, opponentFactory, false, false));
    }

    @Test
    void isValidCastling_IsOppoment_Invalid() {
        Field from = new Field().setPiece(new King().setColor(Color.WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field().setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field().setPiece(new Rook().setColor(Color.WHITE)).setCoordinates(rookCoordinates);

        List<Field> grid = new ArrayList<>();
        grid.add(rookField);

        doReturn(rookField).when(gridLogic).getField(grid, rookCoordinates);

        assertFalse(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_RIGHT, opponentFactory, true, false));
    }
}