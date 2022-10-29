package org.mark.chess.piece.maybecapturedenpassant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.Chessboard;
import org.mark.chess.piece.Bishop;
import org.mark.chess.piece.Pawn;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class PawnIsNotValidBaselineMoveRuleTest {

    @InjectMocks
    private PawnIsNotValidBaselineMoveRule pawnIsNotValidBaselineMoveRule;

    @Test
    void testProcess_WhenIsNotBaselineMove_ThenReturnTrue() {
        Field from = new Field(new Bishop(WHITE)).setCode("e2");
        Field to = new Field(new Pawn(WHITE)).setCode("e3");

        Chessboard chessboard = Chessboard.create();
        chessboard.getFields().set(from.getId(), from);
        chessboard.getFields().set(to.getId(), to);

        assertTrue(pawnIsNotValidBaselineMoveRule.isApplicable(new IsValidMoveParameter(chessboard, from, to, false)));
        assertFalse(pawnIsNotValidBaselineMoveRule.create());
    }

    @Test
    void testProcess_WhenNotFriendlyFire_ThenReturnFalse() {
        Field from = new Field(new Bishop(WHITE)).setCode("e2");
        Field to = new Field(new Pawn(WHITE)).setCode("e4");

        Chessboard chessboard = Chessboard.create();
        chessboard.getFields().set(from.getId(), from);
        chessboard.getFields().set(to.getId(), to);

        assertFalse(pawnIsNotValidBaselineMoveRule.isApplicable(new IsValidMoveParameter(chessboard, from, to, false)));
    }
}