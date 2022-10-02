package org.mark.chess.piece.maybecapturedenpassant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.piece.Bishop;
import org.mark.chess.piece.Pawn;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class PawnHasOpponentPawnAsNeighbourRuleTest {

    @InjectMocks
    private PawnHasOpponentPawnAsNeighbourRule pawnHasOpponentPawnAsNeighbourRule;

    @Test
    void testProcess_WhenPawnHasNotOpponentPawnAsNeighbour_ThenReturnFalse() {
        Field from = new Field(new Bishop(WHITE)).setCode("e2");
        Field to = new Field(new Pawn(WHITE)).setCode("e3");
        Field opponentField = new Field(new Pawn(BLACK)).setCode("c3");

        Grid grid = Grid.create();
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertFalse(pawnHasOpponentPawnAsNeighbourRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
    }

    @Test
    void testProcess_WhenPawnHasOpponentPawnAsNeighbour_ThenReturnTrue() {
        Field from = new Field(new Bishop(WHITE)).setCode("e2");
        Field to = new Field(new Pawn(WHITE)).setCode("e3");
        Field opponentField = new Field(new Pawn(BLACK)).setCode("d3");

        Grid grid = Grid.create();
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertTrue(pawnHasOpponentPawnAsNeighbourRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(pawnHasOpponentPawnAsNeighbourRule.create());
    }
}