package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Grid;
import org.mark.chess.board.Field;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.Bishop;
import org.mark.chess.piece.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.Pawn;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class IsFriendlyFireRuleTest {
    @InjectMocks
    private IsFriendlyFireRule isFriendlyFireRule;

    @Mock
    private Board board;

    @Test
    void testProcess_WhenFriendlyFire_ThenReturnTrue() {
        Field from = new Field(new Bishop(WHITE)).setCode("a1");
        Field to = new Field(new Pawn(WHITE)).setCode("c3");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);

        assertTrue(isFriendlyFireRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertFalse(isFriendlyFireRule.create());
    }

    @Test
    void testProcess_WhenNotFriendlyFire_ThenReturnFalse() {
        Field from = new Field(new Bishop(WHITE)).setCode("a1");
        Field to = new Field(null).setCode("c3");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);

        assertFalse(isFriendlyFireRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
    }
}