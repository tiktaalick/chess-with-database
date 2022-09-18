package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.BISHOP;
import static org.mark.chess.enums.PieceType.PAWN;

@ExtendWith(MockitoExtension.class)
class IsJumpingRuleTest {
    private static final int LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private IsJumpingRule isJumpingRule;

    @Mock
    private Board board;

    @Test
    void testProcess_WhenJumping_ThenReturnTrue() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("a1");
        Field to = new Field(null).setCode("c3");
        Field opponentField = new Field(PAWN.createPiece(BLACK)).setCode("b2");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertTrue(isJumpingRule.test(new IsValidMoveParameter(grid, from, to, false)));
        assertFalse(isJumpingRule.create());
    }

    @Test
    void testProcess_WhenNotJumping_ThenReturnFalse() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("a1");
        Field to = new Field(null).setCode("c3");
        Field opponentField = new Field(PAWN.createPiece(BLACK)).setCode("d4");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertFalse(isJumpingRule.test(new IsValidMoveParameter(grid, from, to, false)));
    }
}