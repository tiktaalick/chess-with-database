package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.piece.Pawn;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.PawnIsValidCaptureMoveRule;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class PawnIsValidCaptureMoveRuleTest {

    @InjectMocks
    private PawnIsValidCaptureMoveRule pawnIsValidCaptureMoveRule;

    @Mock
    private Board board;

    @Mock
    private Button button;

    @Test
    void testRule_WhenInvalidBaselineMove_ThenReturnFalse() {
        Field from = new Field(new Pawn(WHITE)).setCode("e5");
        Field to = new Field(null).setCode("d6");

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);

        assertFalse(pawnIsValidCaptureMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
    }

    @Test
    void testRule_WhenValidBaselineMove_ThenReturnTrue() {
        Field from = new Field(new Pawn(WHITE)).setCode("e5");
        Field to = new Field(new Pawn(BLACK)).setCode("d6");

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);

        assertTrue(pawnIsValidCaptureMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(pawnIsValidCaptureMoveRule.create());
    }
}