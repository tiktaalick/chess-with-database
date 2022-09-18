package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.King;
import org.mark.chess.model.Rook;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.KING;

@ExtendWith(MockitoExtension.class)
class KingIsValidCastlingRuleTest {
    private static final int LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private KingIsValidCastlingRule kingIsValidCastlingRule;

    @Mock
    private Board board;

    @Test
    void testIsValidCastling_CastlingLeft_Valid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(3, 1));

        Coordinates rookCoordinates = new Coordinates(1, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(rookField.getId(), rookField);

        assertTrue(kingIsValidCastlingRule.test(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(kingIsValidCastlingRule.create());
    }

    @Test
    void testIsValidCastling_CastlingRight_Valid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(rookField.getId(), rookField);

        assertTrue(kingIsValidCastlingRule.test(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(kingIsValidCastlingRule.create());
    }

    @Test
    void testIsValidCastling_KingHasAlreadyMoved_Invalid() {
        Field from = new Field(new King(WHITE).setHasMovedAtLeastOnce(true)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(rookField.getId(), rookField);

        assertFalse(kingIsValidCastlingRule.test(new IsValidMoveParameter(grid, from, to, false)));
    }

    @Test
    void testIsValidCastling_RookHasAlreadyMoved_Invalid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE).setHasMovedAtLeastOnce(true)).setCoordinates(rookCoordinates);

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(rookField.getId(), rookField);

        assertFalse(kingIsValidCastlingRule.test(new IsValidMoveParameter(grid, from, to, false)));
    }

    @Test
    void testRule_WhenInvalidCastling_ThenReturnFalse() {
        Field from = new Field(KING.createPiece(WHITE)).setCode("e1");
        Field to = new Field(null).setCode("d1");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);

        assertFalse(kingIsValidCastlingRule.test(new IsValidMoveParameter(grid, from, to, false)));
    }
}