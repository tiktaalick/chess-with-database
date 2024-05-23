package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.general.isvalidmove.IsNotAValidMoveRule;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class IsNotAValidMoveRuleTest {

    @InjectMocks
    IsNotAValidMoveRule isNotAValidMoveRule;

    @Test
    void testRule_Always_ReturnTrue() {
        assertTrue(isNotAValidMoveRule.stopProcessingfurtherRulesAndGetResultNow(null));
        assertFalse(isNotAValidMoveRule.getResult());
    }
}
