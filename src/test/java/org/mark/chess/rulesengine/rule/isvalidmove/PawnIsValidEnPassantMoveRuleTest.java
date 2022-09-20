package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Pawn;
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
class PawnIsValidEnPassantMoveRuleTest {
    private static final int LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private PawnIsValidEnPassantMoveRule pawnIsValidEnPassantMoveRule;

    @Mock
    private Board board;

    @Test
    void testRule_WhenInvalidEnPassantMove_ThenReturnFalse() {
        Field from = new Field(PAWN.createPiece(WHITE)).setCode("e5");
        Field to = new Field(null).setCode("d6");
        Field opponentField = new Field(PAWN.createPiece(BLACK)).setCode("d5");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertFalse(pawnIsValidEnPassantMoveRule.test(new IsValidMoveParameter(grid, from, to, false)));
    }

    @Test
    void testRule_WhenValidEnPassantMove_ThenReturnTrue() {
        Field from = new Field(PAWN.createPiece(WHITE)).setCode("e5");
        Field to = new Field(null).setCode("d6");
        Field opponentField = new Field(((Pawn) PAWN.createPiece(BLACK)).setMayBeCapturedEnPassant(true)).setCode("d5");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertTrue(pawnIsValidEnPassantMoveRule.test(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(pawnIsValidEnPassantMoveRule.create());
    }
}