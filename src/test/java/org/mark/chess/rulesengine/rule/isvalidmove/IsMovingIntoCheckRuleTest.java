package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Grid;
import org.mark.chess.board.Field;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.Bishop;
import org.mark.chess.piece.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.King;
import org.mark.chess.piece.Queen;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class IsMovingIntoCheckRuleTest {
    @InjectMocks
    private IsMovingIntoCheckRule isMovingIntoCheckRule;

    @Mock
    private Board board;

    @Test
    void testProcess_WhenMovingIntoCheck_ThenReturnTrue() {
        Field from = new Field(new Bishop(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("c1");
        Field opponentField = new Field(new Queen(BLACK)).setCode("e4");
        Field kingField = new Field(new King(WHITE)).setCode("e1");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(kingField.getId(), kingField);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertTrue(isMovingIntoCheckRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertFalse(isMovingIntoCheckRule.create());
    }

    @Test
    void testProcess_WhenNotMovingIntoCheck_ThenReturnFalse() {
        Field from = new Field(new Bishop(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("c1");
        Field opponentField = new Field(new Queen(BLACK)).setCode("d4");
        Field kingField = new Field(new King(WHITE)).setCode("e1");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(kingField.getId(), kingField);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertFalse(isMovingIntoCheckRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
    }
}