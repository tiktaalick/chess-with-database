package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.logic.CheckLogic;
import org.mark.chess.logic.MoveLogic;
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
    private final Field field = new Field(null).setCode("a1");

    @InjectMocks
    private HasEmptyParametersRule hasEmptyParametersRule;

    @Mock
    private Grid grid;

    @Mock
    private CheckLogic checkLogic;

    @Mock
    private MoveLogic moveLogic;

    @Test
    void testProcess_WhenEmptyCheckLogic_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.test(new IsValidMoveParameter(grid, field, field, null, false)));
        assertFalse(hasEmptyParametersRule.create());
    }

    @Test
    void testProcess_WhenEmptyFrom_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.test(new IsValidMoveParameter(grid, null, field, checkLogic, false)));
        assertFalse(hasEmptyParametersRule.create());
    }

    @Test
    void testProcess_WhenEmptyGrid_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.test(new IsValidMoveParameter(null, field, field, checkLogic, false)));
        assertFalse(hasEmptyParametersRule.create());
    }

    @Test
    void testProcess_WhenEmptyParameter_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.test(null));
        assertFalse(hasEmptyParametersRule.create());
    }

    @Test
    void testProcess_WhenEmptyTo_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.test(new IsValidMoveParameter(grid, field, null, checkLogic, false)));
        assertFalse(hasEmptyParametersRule.create());
    }

    @Test
    void testProcess_WhenFilledParameters_ThenReturnFalse() {
        assertFalse(hasEmptyParametersRule.test(new IsValidMoveParameter(grid, field, field, checkLogic, false)));
    }
}