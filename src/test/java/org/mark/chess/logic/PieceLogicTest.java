package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Queen;
import org.mark.chess.service.GameService;
import org.mockito.Mock;
import org.mockito.Spy;
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
    private static final char DELIMITER     = ';';
    private static final int  MAX_SQUARE_ID = 63;

    @Spy
    private PieceLogic pieceLogic;

    @Mock
    private PieceLogicFactory opponentFactory;

    @Mock
    private GameService gameService;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private CheckLogic checkLogic;

    @Test
    void testGetAbsoluteHorizontalMove() {
        assertEquals(3,
                pieceLogic.getAbsoluteHorizontalMove(new Field().setCoordinates(new Coordinates(2, 3)),
                        new Field().setCoordinates(new Coordinates(5, 8))));
    }

    @Test
    void testGetAbsoluteVerticalMove() {
        assertEquals(5,
                pieceLogic.getAbsoluteVerticalMove(new Field().setCoordinates(new Coordinates(2, 3)),
                        new Field().setCoordinates(new Coordinates(5, 8))));
    }

    @Test
    void testGetValidMoves_WhenNoValidMoves_ThenReturnOnlyFromMove() {
        Field from = new Field().setCoordinates(new Coordinates(0, 0));
        Field invalidTo = new Field().setCoordinates(new Coordinates(2, 2));

        Grid grid = new Grid(Arrays.asList(from, invalidTo), gridLogic);

        when(pieceLogic.isValidMove(grid, from, from, false)).thenReturn(false);
        when(pieceLogic.isValidMove(grid, from, invalidTo, false)).thenReturn(false);

        List<Field> validMoves = pieceLogic.getValidMoves(grid, from);

        assertEquals(0, validMoves.size());
    }

    @Test
    void testGetValidMoves_WhenOneValidMove_ThenReturnValidMoveAndFromMove() {
        Field from = new Field().setCoordinates(new Coordinates(0, 0));
        Field validTo = new Field().setCoordinates(new Coordinates(1, 1));
        Field invalidTo = new Field().setCoordinates(new Coordinates(2, 2));

        Grid grid = new Grid(Arrays.asList(from, validTo, invalidTo), gridLogic);

        when(pieceLogic.isValidMove(grid, from, from, false)).thenReturn(false);
        when(pieceLogic.isValidMove(grid, from, validTo, false)).thenReturn(true);
        when(pieceLogic.isValidMove(grid, from, invalidTo, false)).thenReturn(false);

        List<Field> validMoves = pieceLogic.getValidMoves(grid, from);

        assertEquals(1, validMoves.size());
    }

    @Test
    void testHasEmptyParameters_WhenEmptyFrom_ThenTrue() {
        assertTrue(pieceLogic.hasEmptyParameters(new Grid(new ArrayList<>(), gridLogic), null, new Field()));
    }

    @Test
    void testHasEmptyParameters_WhenEmptyGrid_ThenTrue() {
        assertTrue(pieceLogic.hasEmptyParameters(null, new Field(), new Field()));
    }

    @Test
    void testHasEmptyParameters_WhenEmptyTo_ThenTrue() {
        assertTrue(pieceLogic.hasEmptyParameters(new Grid(new ArrayList<>(), gridLogic), new Field(), null));
    }

    @Test
    void testHasEmptyParameters_WhenNoEmptyParameters_ThenFalse() {
        assertFalse(pieceLogic.hasEmptyParameters(new Grid(new ArrayList<>(), gridLogic), new Field(), new Field()));
    }

    @Test
    void testIsFriendlyFire_WhenPawnMovesToEmptyField_ThenReturnFalse() {
        assertFalse(pieceLogic.isFriendlyFire(new Pawn().setColor(WHITE), new Field()));
    }

    @Test
    void testIsFriendlyFire_WhenPawnMovesToFieldWithBlackQueen_ThenReturnFalse() {
        assertFalse(pieceLogic.isFriendlyFire(new Pawn().setColor(WHITE), new Field().setPiece(new Queen().setColor(BLACK))));
    }

    @Test
    void testIsFriendlyFire_WhenPawnMovesToFieldWithWhiteQueen_ThenReturnTrue() {
        assertTrue(pieceLogic.isFriendlyFire(new Pawn().setColor(WHITE), new Field().setPiece(new Queen().setColor(WHITE))));
    }

    @Test
    void testIsJumping_WhenWhitePawnMovesTwoStepsAndDoesNotJumpOverAPiece_ThenReturnFalse() {
        Grid grid = new Grid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field().setId(id)).collect(Collectors.toList()), gridLogic);

        Field from = grid
                .getFields()
                .stream()
                .filter(field -> field.getCode().equals("e2"))
                .findFirst()
                .orElse(new Field())
                .setPiece(new Pawn().setColor(WHITE));
        Field to = grid.getFields().stream().filter(field -> field.getCode().equals("e4")).findFirst().orElse(null);

        assertNotNull(to);
        assertFalse(pieceLogic.isJumping(grid, from, to));
    }

    @Test
    void testIsJumping_WhenWhitePawnMovesTwoStepsAndEndsOnAPiece_ThenReturnFalse() {
        Grid grid = new Grid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field().setId(id)).collect(Collectors.toList()), gridLogic);

        Field from = grid
                .getFields()
                .stream()
                .filter(field -> field.getCode().equals("e2"))
                .findFirst()
                .orElse(new Field())
                .setPiece(new Pawn().setColor(WHITE));
        Field to = grid.getFields().stream().filter(field -> field.getCode().equals("e4")).findFirst().orElse(null);

        grid.getFields().stream().filter(field -> field.getCode().equals("e4")).findFirst().orElse(new Field()).setPiece(new Pawn().setColor(WHITE));

        assertNotNull(to);
        assertFalse(pieceLogic.isJumping(grid, from, to));
    }

    @Test
    void testIsJumping_WhenWhitePawnMovesTwoStepsAndJumpsOverAPiece_ThenReturnTrue() {
        Grid grid = new Grid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field().setId(id)).collect(Collectors.toList()), gridLogic);

        Field from = grid
                .getFields()
                .stream()
                .filter(field -> field.getCode().equals("e2"))
                .findFirst()
                .orElse(new Field())
                .setPiece(new Pawn().setColor(WHITE));
        Field to = grid.getFields().stream().filter(field -> field.getCode().equals("e4")).findFirst().orElse(null);

        grid.getFields().stream().filter(field -> field.getCode().equals("e3")).findFirst().orElse(new Field()).setPiece(new Pawn().setColor(WHITE));

        assertNotNull(to);
        assertTrue(pieceLogic.isJumping(grid, from, to));
    }
}