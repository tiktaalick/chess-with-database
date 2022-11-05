package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.Chessboard;
import org.mark.chess.piece.Bishop;
import org.mark.chess.piece.Pawn;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class IsJumpingRuleTest {

    private static final int LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private IsJumpingRule isJumpingRule;

    @Test
    void testProcess_WhenJumping_ThenReturnTrue() {
        Field from = new Field(new Bishop(WHITE)).setCode("a1");
        Field to = new Field(null).setCode("c3");
        Field opponentField = new Field(new Pawn(BLACK)).setCode("b2");

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);
        chessboard.getFields().set(opponentField.getId(), opponentField);

        assertTrue(isJumpingRule.hasResult(new IsValidMoveParameter(chessboard, from, to, false)));
        assertFalse(isJumpingRule.createResult());
    }

    @Test
    void testProcess_WhenNotJumping_ThenReturnFalse() {
        Field from = new Field(new Bishop(WHITE)).setCode("a1");
        Field to = new Field(null).setCode("c3");
        Field opponentField = new Field(new Pawn(BLACK)).setCode("d4");

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);
        chessboard.getFields().set(opponentField.getId(), opponentField);

        assertFalse(isJumpingRule.hasResult(new IsValidMoveParameter(chessboard, from, to, false)));
    }
}