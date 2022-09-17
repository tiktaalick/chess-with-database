package org.mark.chess.rulesengine.rule.piecetype;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.logic.CheckLogic;
import org.mark.chess.logic.GridLogic;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class IsEmptyParametersRuleTest {
    @InjectMocks
    private IsEmptyParametersRule isEmptyParametersRule;

    @Mock
    private Grid grid;

    @Mock
    private Field field;

    @Mock
    private CheckLogic checkLogic;

    @Mock
    private GridLogic gridLogic;

    @Test
    void testProcess_WhenEmptyCheckLogic_ThenReturnTrue() {
        assertTrue(isEmptyParametersRule.test(new IsValidMoveParameter(grid, field, field, null, gridLogic, false)));
        assertFalse(isEmptyParametersRule.isValidMove);
    }

    @Test
    void testProcess_WhenEmptyFrom_ThenReturnTrue() {
        assertTrue(isEmptyParametersRule.test(new IsValidMoveParameter(grid, null, field, checkLogic, gridLogic, false)));
        assertFalse(isEmptyParametersRule.isValidMove);
    }

    @Test
    void testProcess_WhenEmptyGridLogic_ThenReturnTrue() {
        assertTrue(isEmptyParametersRule.test(new IsValidMoveParameter(grid, field, field, checkLogic, null, false)));
        assertFalse(isEmptyParametersRule.isValidMove);
    }

    @Test
    void testProcess_WhenEmptyGrid_ThenReturnTrue() {
        assertTrue(isEmptyParametersRule.test(new IsValidMoveParameter(null, field, field, checkLogic, gridLogic, false)));
        assertFalse(isEmptyParametersRule.isValidMove);
    }

    @Test
    void testProcess_WhenEmptyParameter_ThenReturnTrue() {
        assertTrue(isEmptyParametersRule.test(null));
        assertFalse(isEmptyParametersRule.isValidMove);
    }

    @Test
    void testProcess_WhenEmptyTo_ThenReturnTrue() {
        assertTrue(isEmptyParametersRule.test(new IsValidMoveParameter(grid, field, null, checkLogic, gridLogic, false)));
        assertFalse(isEmptyParametersRule.isValidMove);
    }

    @Test
    void testProcess_WhenFilledParameters_ThenReturnFalse() {
        assertFalse(isEmptyParametersRule.test(new IsValidMoveParameter(grid, field, field, checkLogic, gridLogic, false)));
        assertTrue(isEmptyParametersRule.isValidMove);
    }
}