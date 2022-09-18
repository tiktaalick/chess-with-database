package org.mark.chess.rulesengine.rule.maybecapturedenpassant;

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
class PawnIsNotValidBaselineMoveRuleTest {
    @InjectMocks
    private PawnIsNotValidBaselineMoveRule pawnIsNotValidBaselineMoveRule;

    @Mock
    private Board board;

    @Test
    void testProcess_WhenIsNotBaselineMove_ThenReturnTrue() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("e2");
        Field to = new Field(PAWN.createPiece(WHITE)).setCode("e3");

        Grid grid = Grid.create(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);

        assertTrue(pawnIsNotValidBaselineMoveRule.test(new IsValidMoveParameter(grid, from, to, false)));
        assertFalse(pawnIsNotValidBaselineMoveRule.create());
    }

    @Test
    void testProcess_WhenNotFriendlyFire_ThenReturnFalse() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("e2");
        Field to = new Field(PAWN.createPiece(WHITE)).setCode("e4");

        Grid grid = Grid.create(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);

        assertFalse(pawnIsNotValidBaselineMoveRule.test(new IsValidMoveParameter(grid, from, to, false)));
    }
}