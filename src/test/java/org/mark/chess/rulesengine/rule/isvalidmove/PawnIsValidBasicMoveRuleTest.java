package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.piece.Pawn;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.PawnIsValidBasicMoveRule;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class PawnIsValidBasicMoveRuleTest {

    @InjectMocks
    private PawnIsValidBasicMoveRule pawnIsValidBasicMoveRule;

    @Mock
    private Board board;

    @Test
    void testRule_WhenInvalidBasicMove_ThenReturnFalse() {
        Field from = new Field(new Pawn(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("e5");

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);

        assertFalse(pawnIsValidBasicMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
    }

    @Test
    void testRule_WhenValidBasicMove_ThenReturnTrue() {
        Field from = new Field(new Pawn(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("e4");

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);

        assertTrue(pawnIsValidBasicMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(pawnIsValidBasicMoveRule.create());
    }
}