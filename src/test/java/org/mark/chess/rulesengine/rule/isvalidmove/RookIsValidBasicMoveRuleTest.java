package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.piece.Rook;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.RookIsValidBasicMoveRule;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class RookIsValidBasicMoveRuleTest {

    private static final char DELIMITER = ';';

    @InjectMocks
    private RookIsValidBasicMoveRule rookIsValidBasicMoveRule;

    @Mock
    private Board board;

    @Test
    void testRule_WhenInvalidBasicMove_ThenReturnFalse() {
        Field from = new Field(new Rook(WHITE)).setCode("a1");
        Field to = new Field(null).setCode("h2");

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);

        assertFalse(rookIsValidBasicMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
    }

    @ParameterizedTest
    @CsvSource(value = {"a1;h1", "a8;a1"}, delimiter = DELIMITER)
    void testRule_WhenValidBasicMove_ThenReturnTrue(String codeFrom, String codeTo) {
        Field from = new Field(new Rook(WHITE)).setCode(codeFrom);
        Field to = new Field(null).setCode(codeTo);

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);

        assertTrue(rookIsValidBasicMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(rookIsValidBasicMoveRule.create());
    }
}