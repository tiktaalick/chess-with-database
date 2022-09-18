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
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.BISHOP;
import static org.mark.chess.enums.PieceType.PAWN;

@ExtendWith(MockitoExtension.class)
class PawnHasOpponentPawnAsNeighbourRuleTest {
    @InjectMocks
    private PawnHasOpponentPawnAsNeighbourRule pawnHasOpponentPawnAsNeighbourRule;

    @Mock
    private Board board;

    @Test
    void testProcess_WhenPawnHasNotOpponentPawnAsNeighbour_ThenReturnFalse() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("e2");
        Field to = new Field(PAWN.createPiece(WHITE)).setCode("e3");
        Field opponentField = new Field(PAWN.createPiece(BLACK)).setCode("c3");

        Grid grid = Grid.create(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertFalse(pawnHasOpponentPawnAsNeighbourRule.test(new IsValidMoveParameter(grid, from, to, false)));
    }

    @Test
    void testProcess_WhenPawnHasOpponentPawnAsNeighbour_ThenReturnTrue() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("e2");
        Field to = new Field(PAWN.createPiece(WHITE)).setCode("e3");
        Field opponentField = new Field(PAWN.createPiece(BLACK)).setCode("d3");

        Grid grid = Grid.create(board, WHITE);
        grid.getFields().set(from.getId(), from);
        grid.getFields().set(to.getId(), to);
        grid.getFields().set(opponentField.getId(), opponentField);

        assertTrue(pawnHasOpponentPawnAsNeighbourRule.test(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(pawnHasOpponentPawnAsNeighbourRule.create());
    }
}