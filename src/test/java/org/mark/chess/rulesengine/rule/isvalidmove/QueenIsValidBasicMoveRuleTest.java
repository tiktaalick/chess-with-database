package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.piece.Queen;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.QueenIsValidBasicMoveRule;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class QueenIsValidBasicMoveRuleTest {

    private static final char DELIMITER = ';';

    @InjectMocks
    private QueenIsValidBasicMoveRule queenIsValidBasicMoveRule;

    @Mock
    private Board board;

    @Test
    void testRule_WhenInvalidBasicMove_ThenReturnFalse() {
        Field from = new Field(new Queen(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("c2");

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);

        assertFalse(queenIsValidBasicMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
    }

    @ParameterizedTest
    @CsvSource(value = {"e3;c1", "e3;g1", "a1;a8", "a8;a1"}, delimiter = DELIMITER)
    void testRule_WhenValidBasicMove_ThenReturnTrue(String codeFrom, String codeTo) {
        Field from = new Field(new Queen(WHITE)).setCode(codeFrom);
        Field to = new Field(null).setCode(codeTo);

        Grid grid = Grid.createEmpty();
        grid.getFields().set(from.getId(), from);

        assertTrue(queenIsValidBasicMoveRule.isApplicable(new IsValidMoveParameter(grid, from, to, false)));
        assertTrue(queenIsValidBasicMoveRule.create());
    }
}