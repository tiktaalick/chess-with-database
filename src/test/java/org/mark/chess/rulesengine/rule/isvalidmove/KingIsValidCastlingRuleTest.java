package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.piece.King;
import org.mark.chess.piece.Rook;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.KingIsValidCastlingRule;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class KingIsValidCastlingRuleTest {

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

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(rookField.getId(), rookField);

        assertTrue(kingIsValidCastlingRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(kingIsValidCastlingRule.create());
    }

    @Test
    void testIsValidCastling_CastlingRight_Valid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(rookField.getId(), rookField);

        assertTrue(kingIsValidCastlingRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(kingIsValidCastlingRule.create());
    }

    @Test
    void testIsValidCastling_KingHasAlreadyMoved_Invalid() {
        Field from = new Field(new King(WHITE).setHasMovedAtLeastOnce(true)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(rookField.getId(), rookField);

        assertFalse(kingIsValidCastlingRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
    }

    @Test
    void testIsValidCastling_RookHasAlreadyMoved_Invalid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE).setHasMovedAtLeastOnce(true)).setCoordinates(rookCoordinates);

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(rookField.getId(), rookField);

        assertFalse(kingIsValidCastlingRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
    }

    @Test
    void testRule_WhenInvalidCastling_ThenReturnFalse() {
        Field from = new Field(new King(WHITE)).setCode("e1");
        Field to = new Field(null).setCode("d1");

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);

        assertFalse(kingIsValidCastlingRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
    }
}