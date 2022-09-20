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
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.BISHOP;
import static org.mark.chess.enums.PieceType.PAWN;

@ExtendWith(MockitoExtension.class)
class IsFriendlyFireRuleTest {
    private static final int LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private IsFriendlyFireRule isFriendlyFireRule;

    @Mock
    private Board board;

    @Test
    void testProcess_WhenFriendlyFire_ThenReturnTrue() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("a1");
        Field to = new Field(PAWN.createPiece(WHITE)).setCode("c3");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);

        assertTrue(isFriendlyFireRule.test(new IsValidMoveParameter(grid, from, to, false)));
        assertFalse(isFriendlyFireRule.create());
    }

    @Test
    void testProcess_WhenNotFriendlyFire_ThenReturnFalse() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("a1");
        Field to = new Field(null).setCode("c3");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);

        assertFalse(isFriendlyFireRule.test(new IsValidMoveParameter(grid, from, to, false)));
    }
}