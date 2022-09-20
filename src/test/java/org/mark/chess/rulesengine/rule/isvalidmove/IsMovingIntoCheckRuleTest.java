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
import static org.mark.chess.enums.PieceType.KING;
import static org.mark.chess.enums.PieceType.QUEEN;

@ExtendWith(MockitoExtension.class)
class IsMovingIntoCheckRuleTest {
    @InjectMocks
    private IsMovingIntoCheckRule isMovingIntoCheckRule;

    @Mock
    private Board board;

    @Test
    void testProcess_WhenMovingIntoCheck_ThenReturnTrue() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("c1");
        Field opponentField = new Field(QUEEN.createPiece(BLACK)).setCode("e4");
        Field kingField = new Field(KING.createPiece(WHITE)).setCode("e1");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(kingField.getId(), kingField);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertTrue(isMovingIntoCheckRule.test(new IsValidMoveParameter(grid, from, to, false)));
        assertFalse(isMovingIntoCheckRule.create());
    }

    @Test
    void testProcess_WhenNotMovingIntoCheck_ThenReturnFalse() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("c1");
        Field opponentField = new Field(QUEEN.createPiece(BLACK)).setCode("d4");
        Field kingField = new Field(KING.createPiece(WHITE)).setCode("e1");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(kingField.getId(), kingField);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertFalse(isMovingIntoCheckRule.test(new IsValidMoveParameter(grid, from, to, false)));
    }
}