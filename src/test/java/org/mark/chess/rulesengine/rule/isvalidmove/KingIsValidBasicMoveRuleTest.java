package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.board.Grid;
import org.mark.chess.board.Field;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.Bishop;
import org.mark.chess.piece.isvalidmove.KingIsValidBasicMoveRule;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class KingIsValidBasicMoveRuleTest {
    private static final char DELIMITER = ';';

    @InjectMocks
    private KingIsValidBasicMoveRule kingIsValidBasicMoveRule;

    @Mock
    private Board board;

    @Test
    void testRule_WhenInvalidBasicMove_ThenReturnFalse() {
        Field from = new Field(new Bishop(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("e5");

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);

        assertFalse(kingIsValidBasicMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
    }

    @ParameterizedTest
    @CsvSource(value = {"e3;d4", "e3;e4", "e3;f4", "e3;e2", "e3;e4", "e3;d2", "e3;d3", "e3;d4"}, delimiter = DELIMITER)
    void testRule_WhenValidBasicMove_ThenReturnTrue(String codeFrom, String codeTo) {
        Field from = new Field(new Bishop(WHITE)).setCode(codeFrom);
        Field to = new Field(null).setCode(codeTo);

        Grid grid = Grid.createEmpty(board, WHITE);
        grid.getFields().set(from.getId(), from);

        assertTrue(kingIsValidBasicMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(kingIsValidBasicMoveRule.create());
    }
}