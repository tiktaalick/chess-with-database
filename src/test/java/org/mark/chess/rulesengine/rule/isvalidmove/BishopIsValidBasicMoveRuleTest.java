package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.board.Grid;
import org.mark.chess.board.Field;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.Bishop;
import org.mark.chess.piece.isvalidmove.BishopIsValidBasicMoveRule;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class BishopIsValidBasicMoveRuleTest {
    private static final char DELIMITER = ';';

    @InjectMocks
    private BishopIsValidBasicMoveRule bishopIsValidBasicMoveRule;

    @Mock
    private Board board;

    @Test
    void testRule_WhenInvalidBasicMove_ThenReturnFalse() {
        Field from = new Field(new Bishop(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("c2");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);

        assertFalse(bishopIsValidBasicMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
    }

    @ParameterizedTest
    @CsvSource(value = {"e3;c1", "e3;g1"}, delimiter = DELIMITER)
    void testRule_WhenValidBasicMove_ThenReturnTrue(String codeFrom, String codeTo) {
        Field from = new Field(new Bishop(WHITE)).setCode(codeFrom);
        Field to = new Field(null).setCode(codeTo);

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);

        assertTrue(bishopIsValidBasicMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(bishopIsValidBasicMoveRule.create());
    }
}