package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.board.Chessboard;
import org.mark.chess.piece.King;
import org.mark.chess.piece.Rook;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class KingIsValidCastlingRuleTest {

    @InjectMocks
    private KingIsValidCastlingRule kingIsValidCastlingRule;

    @Test
    void testIsValidCastling_CastlingLeft_Valid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(3, 1));

        Coordinates rookCoordinates = new Coordinates(1, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);
        chessboard.getFields().set(rookField.getId(), rookField);

        assertTrue(kingIsValidCastlingRule.hasResult(new IsValidMoveParameter(chessboard, from, to, false)));
        assertTrue(kingIsValidCastlingRule.createResult());
    }

    @Test
    void testIsValidCastling_CastlingRight_Valid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);
        chessboard.getFields().set(rookField.getId(), rookField);

        assertTrue(kingIsValidCastlingRule.hasResult(new IsValidMoveParameter(chessboard, from, to, false)));
        assertTrue(kingIsValidCastlingRule.createResult());
    }

    @Test
    void testIsValidCastling_KingHasAlreadyMoved_Invalid() {
        Field from = new Field(new King(WHITE).setHasMovedAtLeastOnce(true)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);
        chessboard.getFields().set(rookField.getId(), rookField);

        assertFalse(kingIsValidCastlingRule.hasResult(new IsValidMoveParameter(chessboard, from, to, false)));
    }

    @Test
    void testIsValidCastling_RookHasAlreadyMoved_Invalid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE).setHasMovedAtLeastOnce(true)).setCoordinates(rookCoordinates);

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);
        chessboard.getFields().set(rookField.getId(), rookField);

        assertFalse(kingIsValidCastlingRule.hasResult(new IsValidMoveParameter(chessboard, from, to, false)));
    }

    @Test
    void testRule_WhenInvalidCastling_ThenReturnFalse() {
        Field from = new Field(new King(WHITE)).setCode("e1");
        Field to = new Field(null).setCode("d1");

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);

        assertFalse(kingIsValidCastlingRule.hasResult(new IsValidMoveParameter(chessboard, from, to, false)));
    }
}