package org.mark.chess.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Queen;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PieceLogicTest {
    private static final int MAX_SQUARE_ID = 63;

    @Mock
    private GridLogic gridLogic;

    private PieceLogic pieceLogic;

    @BeforeEach
    void beforeEach() {
        pieceLogic = Mockito.mock(PieceLogic.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    void testGetAbsoluteHorizontalMove() {
        assertEquals(3,
                pieceLogic.getAbsoluteHorizontalMove(new Field(null).setCoordinates(new Coordinates(2, 3)),
                        new Field(null).setCoordinates(new Coordinates(5, 8))));
    }

    @Test
    void testGetAbsoluteVerticalMove() {
        assertEquals(5,
                pieceLogic.getAbsoluteVerticalMove(new Field(null).setCoordinates(new Coordinates(2, 3)),
                        new Field(null).setCoordinates(new Coordinates(5, 8))));
    }

    @Test
    void testGetValidMoves_WhenNoValidMoves_ThenReturnOnlyFromMove() {
        Field from = new Field(null).setCoordinates(new Coordinates(0, 0));
        Field invalidTo = new Field(null).setCoordinates(new Coordinates(2, 2));

        Grid grid = Grid.createGrid(Arrays.asList(from, invalidTo), gridLogic);

        when(pieceLogic.isValidMove(grid, from, from, false)).thenReturn(false);
        when(pieceLogic.isValidMove(grid, from, invalidTo, false)).thenReturn(false);

        List<Field> validMoves = pieceLogic.getValidMoves(grid, from);

        assertEquals(0, validMoves.size());
    }

    @Test
    void testGetValidMoves_WhenOneValidMove_ThenReturnValidMoveAndFromMove() {
        Field from = new Field(null).setCoordinates(new Coordinates(0, 0));
        Field validTo = new Field(null).setCoordinates(new Coordinates(1, 1));
        Field invalidTo = new Field(null).setCoordinates(new Coordinates(2, 2));

        Grid grid = Grid.createGrid(Arrays.asList(from, validTo, invalidTo), gridLogic);

        when(pieceLogic.isValidMove(grid, from, from, false)).thenReturn(false);
        when(pieceLogic.isValidMove(grid, from, validTo, false)).thenReturn(true);
        when(pieceLogic.isValidMove(grid, from, invalidTo, false)).thenReturn(false);

        List<Field> validMoves = pieceLogic.getValidMoves(grid, from);

        assertEquals(1, validMoves.size());
    }

    @Test
    void testHasEmptyParameters_WhenEmptyFrom_ThenTrue() {
        assertTrue(pieceLogic.hasEmptyParameters(Grid.createGrid(new ArrayList<>(), gridLogic), null, new Field(null)));
    }

    @Test
    void testHasEmptyParameters_WhenEmptyGrid_ThenTrue() {
        assertTrue(pieceLogic.hasEmptyParameters(null, new Field(null), new Field(null)));
    }

    @Test
    void testHasEmptyParameters_WhenEmptyTo_ThenTrue() {
        assertTrue(pieceLogic.hasEmptyParameters(Grid.createGrid(new ArrayList<>(), gridLogic), new Field(null), null));
    }

    @Test
    void testHasEmptyParameters_WhenNoEmptyParameters_ThenFalse() {
        assertFalse(pieceLogic.hasEmptyParameters(Grid.createGrid(new ArrayList<>(), gridLogic), new Field(null), new Field(null)));
    }

    @Test
    void testIsFriendlyFire_WhenPawnMovesToEmptyField_ThenReturnFalse() {
        assertFalse(pieceLogic.isFriendlyFire(new Pawn(WHITE), new Field(null)));
    }

    @Test
    void testIsFriendlyFire_WhenPawnMovesToFieldWithBlackQueen_ThenReturnFalse() {
        assertFalse(pieceLogic.isFriendlyFire(new Pawn(WHITE), new Field(new Queen(BLACK))));
    }

    @Test
    void testIsFriendlyFire_WhenPawnMovesToFieldWithWhiteQueen_ThenReturnTrue() {
        assertTrue(pieceLogic.isFriendlyFire(new Pawn(WHITE), new Field(new Queen(WHITE))));
    }

    @Test
    void testIsJumping_WhenWhitePawnMovesTwoStepsAndDoesNotJumpOverAPiece_ThenReturnFalse() {
        Grid grid = Grid.createGrid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()),
                gridLogic);

        Field from = grid
                .getFields()
                .stream()
                .filter(field -> field.getCode().equals("e2"))
                .findFirst()
                .orElse(new Field(null))
                .setPiece(new Pawn(WHITE));
        Field to = grid.getFields().stream().filter(field -> field.getCode().equals("e4")).findFirst().orElse(null);

        assertNotNull(to);
        assertFalse(pieceLogic.isJumping(grid, from, to));
    }

    @Test
    void testIsJumping_WhenWhitePawnMovesTwoStepsAndEndsOnAPiece_ThenReturnFalse() {
        Grid grid = Grid.createGrid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()),
                gridLogic);

        Field from = grid
                .getFields()
                .stream()
                .filter(field -> field.getCode().equals("e2"))
                .findFirst()
                .orElse(new Field(null))
                .setPiece(new Pawn(WHITE));
        Field to = grid.getFields().stream().filter(field -> field.getCode().equals("e4")).findFirst().orElse(null);

        grid.getFields().stream().filter(field -> field.getCode().equals("e4")).findFirst().orElse(new Field(null)).setPiece(new Pawn(WHITE));

        assertNotNull(to);
        assertFalse(pieceLogic.isJumping(grid, from, to));
    }

    @Test
    void testIsJumping_WhenWhitePawnMovesTwoStepsAndJumpsOverAPiece_ThenReturnTrue() {
        Grid grid = Grid.createGrid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()),
                gridLogic);

        Field from = grid
                .getFields()
                .stream()
                .filter(field -> field.getCode().equals("e2"))
                .findFirst()
                .orElse(new Field(null))
                .setPiece(new Pawn(WHITE));
        Field to = grid.getFields().stream().filter(field -> field.getCode().equals("e4")).findFirst().orElse(null);

        grid.getFields().stream().filter(field -> field.getCode().equals("e3")).findFirst().orElse(new Field(null)).setPiece(new Pawn(WHITE));

        assertNotNull(to);
        assertTrue(pieceLogic.isJumping(grid, from, to));
    }
}