package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.Chessboard;
import org.mark.chess.piece.Pawn;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class PawnIsValidBaselineMoveRuleTest {

    private static final int LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private PawnIsValidBaselineMoveRule pawnIsValidBaselineMoveRule;

    @Test
    void testRule_WhenInvalidBaselineMove_ThenReturnFalse() {
        Field from = new Field(new Pawn(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("e5");

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);

        assertFalse(pawnIsValidBaselineMoveRule.hasResult(new IsValidMoveParameter(chessboard, from, to, false)));
    }

    @Test
    void testRule_WhenValidBaselineMove_ThenReturnTrue() {
        Field from = new Field(new Pawn(WHITE)).setCode("e2");
        Field to = new Field(null).setCode("e4");

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);

        assertTrue(pawnIsValidBaselineMoveRule.hasResult(new IsValidMoveParameter(chessboard, from, to, false)));
        assertTrue(pawnIsValidBaselineMoveRule.createResult());
    }
}