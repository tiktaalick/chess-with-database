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
import static org.mark.chess.enums.PieceType.PAWN;

@ExtendWith(MockitoExtension.class)
class PawnIsNotValidDirectionRuleTest {
    private static final int LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private PawnIsNotValidDirectionRule pawnIsNotValidDirectionRule;

    @Mock
    private Board board;

    @Test
    void testRule_WhenInvalidDirection_ThenReturnTrue() {
        Field from = new Field(PAWN.createPiece(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("e2");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);

        assertTrue(pawnIsNotValidDirectionRule.test(new IsValidMoveParameter(grid, from, to, false)));
        assertFalse(pawnIsNotValidDirectionRule.create());
    }

    @Test
    void testRule_WhenValidDirection_ThenReturnFalse() {
        Field from = new Field(PAWN.createPiece(BLACK)).setCode("e3");
        Field to = new Field(null).setCode("e2");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);

        assertFalse(pawnIsNotValidDirectionRule.test(new IsValidMoveParameter(grid, from, to, false)));
        assertFalse(pawnIsNotValidDirectionRule.create());
    }
}