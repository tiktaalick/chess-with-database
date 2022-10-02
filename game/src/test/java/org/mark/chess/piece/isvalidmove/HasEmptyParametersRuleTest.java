package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Grid;
import org.mark.chess.board.Field;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.HasEmptyParametersRule;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class HasEmptyParametersRuleTest {
    private final Field field = new Field(null).setCode("a1");

    @InjectMocks
    private HasEmptyParametersRule hasEmptyParametersRule;

    @Mock
    private Grid grid;

    @Test
    void testProcess_WhenEmptyFrom_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.isApplicable(new IsValidMoveParameter(grid, null, field, false)));
        assertFalse(hasEmptyParametersRule.create());
    }

    @Test
    void testProcess_WhenEmptyGrid_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.isApplicable(new IsValidMoveParameter(null, field, field, false)));
        assertFalse(hasEmptyParametersRule.create());
    }

    @Test
    void testProcess_WhenEmptyParameter_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.isApplicable(null));
        assertFalse(hasEmptyParametersRule.create());
    }

    @Test
    void testProcess_WhenEmptyTo_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.isApplicable(new IsValidMoveParameter(grid, field, null, false)));
        assertFalse(hasEmptyParametersRule.create());
    }

    @Test
    void testProcess_WhenFilledParameters_ThenReturnFalse() {
        assertFalse(hasEmptyParametersRule.isApplicable(new IsValidMoveParameter(grid, field, field, false)));
    }
}