package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.isvalidmove.IsNotValidRule;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class IsNotValidRuleTest {
    @InjectMocks
    IsNotValidRule isNotValidRule;

    @Test
    void testRule_Always_ReturnTrue() {
        assertTrue(isNotValidRule.isApplicable(null));
        assertFalse(isNotValidRule.create());
    }
}