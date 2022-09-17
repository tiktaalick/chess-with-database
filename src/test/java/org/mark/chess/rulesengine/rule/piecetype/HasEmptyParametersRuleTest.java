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
class HasEmptyParametersRuleTest {
    @InjectMocks
    private HasEmptyParametersRule hasEmptyParametersRule;

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
        assertTrue(hasEmptyParametersRule.test(new IsValidMoveParameter(grid, field, field, null, gridLogic, false)));
        assertFalse(hasEmptyParametersRule.isValidMove);
    }

    @Test
    void testProcess_WhenEmptyFrom_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.test(new IsValidMoveParameter(grid, null, field, checkLogic, gridLogic, false)));
        assertFalse(hasEmptyParametersRule.isValidMove);
    }

    @Test
    void testProcess_WhenEmptyGridLogic_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.test(new IsValidMoveParameter(grid, field, field, checkLogic, null, false)));
        assertFalse(hasEmptyParametersRule.isValidMove);
    }

    @Test
    void testProcess_WhenEmptyGrid_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.test(new IsValidMoveParameter(null, field, field, checkLogic, gridLogic, false)));
        assertFalse(hasEmptyParametersRule.isValidMove);
    }

    @Test
    void testProcess_WhenEmptyParameter_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.test(null));
        assertFalse(hasEmptyParametersRule.isValidMove);
    }

    @Test
    void testProcess_WhenEmptyTo_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.test(new IsValidMoveParameter(grid, field, null, checkLogic, gridLogic, false)));
        assertFalse(hasEmptyParametersRule.isValidMove);
    }

    @Test
    void testProcess_WhenFilledParameters_ThenReturnFalse() {
        assertFalse(hasEmptyParametersRule.test(new IsValidMoveParameter(grid, field, field, checkLogic, gridLogic, false)));
        assertTrue(hasEmptyParametersRule.isValidMove);
    }
}