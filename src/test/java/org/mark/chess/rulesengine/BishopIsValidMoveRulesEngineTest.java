package org.mark.chess.rulesengine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.rulesengine.rule.piecetype.BishopIsValidBasicMoveRule;
import org.mark.chess.rulesengine.rule.piecetype.IsEmptyParametersRule;
import org.mark.chess.rulesengine.rule.piecetype.IsFriendlyFireRule;
import org.mark.chess.rulesengine.rule.piecetype.IsJumpingRule;
import org.mark.chess.rulesengine.rule.piecetype.IsMovingIntoCheckRule;
import org.mark.chess.rulesengine.rule.piecetype.IsNotValidRule;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BishopIsValidMoveRulesEngineTest {
    @InjectMocks
    private BishopIsValidMoveRulesEngine bishopIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertTrue(bishopIsValidMoveRulesEngine.rules.get(0) instanceof IsEmptyParametersRule);
        assertTrue(bishopIsValidMoveRulesEngine.rules.get(1) instanceof IsFriendlyFireRule);
        assertTrue(bishopIsValidMoveRulesEngine.rules.get(2) instanceof IsJumpingRule);
        assertTrue(bishopIsValidMoveRulesEngine.rules.get(3) instanceof IsMovingIntoCheckRule);
        assertTrue(bishopIsValidMoveRulesEngine.rules.get(4) instanceof BishopIsValidBasicMoveRule);
        assertTrue(bishopIsValidMoveRulesEngine.rules.get(5) instanceof IsNotValidRule);
    }
}