package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Field;
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
    private Chessboard chessboard;

    @Test
    void testProcess_WhenEmptyFrom_ThenReturnTrue() {
        assertTrue(hasEmptyParametersRule.isApplicable(new IsValidMoveParameter(chessboard, null, field, false)));
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
        assertTrue(hasEmptyParametersRule.isApplicable(new IsValidMoveParameter(chessboard, field, null, false)));
        assertFalse(hasEmptyParametersRule.create());
    }

    @Test
    void testProcess_WhenFilledParameters_ThenReturnFalse() {
        assertFalse(hasEmptyParametersRule.isApplicable(new IsValidMoveParameter(chessboard, field, field, false)));
    }
}