package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.piece.Pawn;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.PawnIsValidEnPassantMoveRule;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class PawnIsValidEnPassantMoveRuleTest {

    @InjectMocks
    private PawnIsValidEnPassantMoveRule pawnIsValidEnPassantMoveRule;

    @Mock
    private Board board;

    @Test
    void testRule_WhenInvalidEnPassantMove_ThenReturnFalse() {
        Field from = new Field(new Pawn(WHITE)).setCode("e5");
        Field to = new Field(null).setCode("d6");
        Field opponentField = new Field(new Pawn(BLACK)).setCode("d5");

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertFalse(pawnIsValidEnPassantMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
    }

    @Test
    void testRule_WhenValidEnPassantMove_ThenReturnTrue() {
        Field from = new Field(new Pawn(WHITE)).setCode("e5");
        Field to = new Field(null).setCode("d6");
        Field opponentField = new Field(((Pawn) new Pawn(BLACK)).setMayBeCapturedEnPassant(true)).setCode("d5");

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertTrue(pawnIsValidEnPassantMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(pawnIsValidEnPassantMoveRule.create());
    }
}