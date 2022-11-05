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
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class PawnIsValidCaptureMoveRuleTest {

    @InjectMocks
    private PawnIsValidCaptureMoveRule pawnIsValidCaptureMoveRule;

    @Test
    void testRule_WhenInvalidBaselineMove_ThenReturnFalse() {
        Field from = new Field(new Pawn(WHITE)).setCode("e5");
        Field to = new Field(null).setCode("d6");

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);
        chessboard.getFields().set(to.getId(), to);

        assertFalse(pawnIsValidCaptureMoveRule.hasResult(new IsValidMoveParameter(chessboard, from, to, false)));
    }

    @Test
    void testRule_WhenValidBaselineMove_ThenReturnTrue() {
        Field from = new Field(new Pawn(WHITE)).setCode("e5");
        Field to = new Field(new Pawn(BLACK)).setCode("d6");

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);
        chessboard.getFields().set(to.getId(), to);

        assertTrue(pawnIsValidCaptureMoveRule.hasResult(new IsValidMoveParameter(chessboard, from, to, false)));
        assertTrue(pawnIsValidCaptureMoveRule.createResult());
    }
}