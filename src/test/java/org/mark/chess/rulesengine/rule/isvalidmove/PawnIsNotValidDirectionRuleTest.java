package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Grid;
import org.mark.chess.board.Field;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.Pawn;
import org.mark.chess.piece.isvalidmove.PawnIsNotValidDirectionRule;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class PawnIsNotValidDirectionRuleTest {
    @InjectMocks
    private PawnIsNotValidDirectionRule pawnIsNotValidDirectionRule;

    @Mock
    private Board board;

    @Test
    void testRule_WhenInvalidDirection_ThenReturnTrue() {
        Field from = new Field(new Pawn(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("e2");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);

        assertTrue(pawnIsNotValidDirectionRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertFalse(pawnIsNotValidDirectionRule.create());
    }

    @Test
    void testRule_WhenValidDirection_ThenReturnFalse() {
        Field from = new Field(new Pawn(BLACK)).setCode("e3");
        Field to = new Field(null).setCode("e2");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);

        assertFalse(pawnIsNotValidDirectionRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertFalse(pawnIsNotValidDirectionRule.create());
    }
}