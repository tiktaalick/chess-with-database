package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.King;
import org.mark.chess.model.PieceTypeLogic;
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.QUEEN;
import static org.mark.chess.enums.PieceType.ROOK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KingLogicTest {
    private static final char        DELIMITER                   = ';';
    private static final Coordinates VALID_MOVE_COORDINATES_FROM = new Coordinates(3, 3);
    private static final Coordinates VALID_MOVE_COORDINATES_TO   = new Coordinates(3, 4);

    @Spy
    @InjectMocks
    private KingLogic kingLogic;

    @Mock
    private PieceTypeLogic pieceTypeLogic;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private RookLogic rookLogic;

    @Mock
    private QueenLogic queenLogic;

    @Mock
    private CheckLogic checkLogic;

    @Mock
    private Board board;

    @Mock
    private Button button;

    @Test
    void testIsValidCastling_CastlingLeft_Valid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(3, 1));

        Coordinates rookCoordinates = new Coordinates(1, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Grid grid = Grid.createGrid(List.of(rookField), gridLogic);

        when(gridLogic.getField(grid, rookCoordinates)).thenReturn(rookField);

        assertTrue(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_LEFT, false, false));
    }

    @Test
    void testIsValidCastling_CastlingRight_Valid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Grid grid = Grid.createGrid(List.of(rookField), gridLogic);

        when(gridLogic.getField(grid, rookCoordinates)).thenReturn(rookField);

        assertTrue(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_RIGHT, false, false));
    }

    @Test
    void testIsValidCastling_IsOppoment_Invalid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Grid grid = Grid.createGrid(List.of(rookField), gridLogic);

        when(gridLogic.getField(grid, rookCoordinates)).thenReturn(rookField);

        assertFalse(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_RIGHT, true, false));
    }

    @Test
    void testIsValidCastling_KingHasAlreadyMoved_Invalid() {
        Field from = new Field(new King(WHITE).setHasMovedAtLeastOnce(true)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Grid grid = Grid.createGrid(List.of(rookField), gridLogic);

        when(gridLogic.getField(grid, rookCoordinates)).thenReturn(rookField);

        assertFalse(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_RIGHT, false, false));
    }

    @Test
    void testIsValidCastling_KingHasMovedDuringCastling_Valid() {
        Field from = new Field(new King(WHITE).setHasMovedAtLeastOnce(true)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Grid grid = Grid.createGrid(List.of(rookField), gridLogic);

        when(gridLogic.getField(grid, rookCoordinates)).thenReturn(rookField);

        assertTrue(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_RIGHT, false, true));
    }

    @Test
    void testIsValidCastling_KingInCheck_Invalid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);
        Field opponentField = new Field(new Rook(BLACK)).setCoordinates(new Coordinates(5, 8));

        from.setButton(button);
        to.setButton(button);
        rookField.setButton(button);
        opponentField.setButton(button);

        Grid grid = Grid.createGrid(Arrays.asList(rookField, opponentField), gridLogic).setOpponentKingField(opponentField);

        when(gridLogic.getField(grid, rookCoordinates)).thenReturn(rookField);
        when(ROOK.getLogic(pieceTypeLogic)).thenReturn(rookLogic);
        when(rookLogic.isValidMove(grid, opponentField, from, true)).thenReturn(Boolean.TRUE);
        when(checkLogic.isInCheckNow(grid, from, from, false)).thenReturn(Boolean.TRUE);

        assertFalse(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_RIGHT, false, false));
    }

    @Test
    void testIsValidCastling_RookHasAlreadyMoved_Invalid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE).setHasMovedAtLeastOnce(true)).setCoordinates(rookCoordinates);

        Grid grid = Grid.createGrid(List.of(rookField), gridLogic);

        when(gridLogic.getField(grid, rookCoordinates)).thenReturn(rookField);

        assertFalse(kingLogic.isValidCastling(grid, from, to, KingLogic.KING_X_RIGHT, false, false));
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
            "3;3;1;2;false",}, delimiter = DELIMITER)
    void testIsValidMove_BasicMoves(int fromX, int fromY, int toX, int toY, boolean expected) {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(fromX, fromY));
        Field to = new Field(null).setCoordinates(new Coordinates(toX, toY));
        Grid grid = Grid.createGrid(new ArrayList<>(), gridLogic);

        when(checkLogic.isMovingIntoCheck(grid, from, to, false, gridLogic)).thenReturn(Boolean.FALSE);

        boolean actual = kingLogic.isValidMove(grid, from, to, false);
        assertEquals(expected, actual);
    }

    @Test
    void testIsValidMove_WhenFriendlyFire_ThenReturnFalse() {
        Field from = new Field(new King(WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field(null).setCoordinates(VALID_MOVE_COORDINATES_TO);
        Grid grid = Grid.createGrid(new ArrayList<>(), gridLogic);

        when(checkLogic.isMovingIntoCheck(grid, from, to, false, gridLogic)).thenReturn(Boolean.FALSE);
        when(kingLogic.isFriendlyFire(from.getPiece(), to)).thenReturn(Boolean.TRUE);

        assertFalse(kingLogic.isValidMove(grid, from, to, false));
    }

    @Test
    void testIsValidMove_WhenInCheck_ThenReturnFalse() {
        Field from = new Field(new King(WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field(null).setCoordinates(VALID_MOVE_COORDINATES_TO);

        Field opponentField = new Field(new Queen(BLACK)).setCoordinates(new Coordinates(4, 4));
        opponentField.setButton(button);
        Grid grid = Grid.createGrid(Arrays.asList(opponentField, from, to), gridLogic);

        when(QUEEN.getLogic(pieceTypeLogic)).thenReturn(queenLogic);
        when(queenLogic.isValidMove(any(Grid.class), eq(opponentField), any(Field.class), eq(true))).thenReturn(Boolean.TRUE);
        when(checkLogic.isMovingIntoCheck(grid, from, to, false, gridLogic)).thenReturn(Boolean.TRUE);

        assertFalse(kingLogic.isValidMove(grid, from, to, false));
    }

    @Test
    void testIsValidMove_WhenJumping_ThenReturnFalse() {
        Field from = new Field(new King(WHITE)).setCoordinates(VALID_MOVE_COORDINATES_FROM);
        Field to = new Field(null).setCoordinates(VALID_MOVE_COORDINATES_TO);
        Grid grid = Grid.createGrid(new ArrayList<>(), gridLogic);

        when(checkLogic.isMovingIntoCheck(grid, from, to, false, gridLogic)).thenReturn(Boolean.FALSE);
        when(kingLogic.isJumping(grid, from, to)).thenReturn(Boolean.TRUE);

        assertFalse(kingLogic.isValidMove(grid, from, to, false));
    }

    @Test
    void testIsValidMove_WhenNullValues_ThenReturnFalse() {
        assertFalse(kingLogic.isValidMove(null, null, null, false));
    }
}