package org.mark.chess.rulesengine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.rulesengine.rule.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsJumpingRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsNotValidRule;
import org.mark.chess.rulesengine.rule.isvalidmove.KingIsValidBasicMoveRule;
import org.mark.chess.rulesengine.rule.isvalidmove.KingIsValidCastlingRule;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class KingIsValidMoveRulesEngineTest {
    @InjectMocks
    private KingIsValidMoveRulesEngine kingIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertTrue(kingIsValidMoveRulesEngine.rules.get(0) instanceof HasEmptyParametersRule);
        assertTrue(kingIsValidMoveRulesEngine.rules.get(1) instanceof IsFriendlyFireRule);
        assertTrue(kingIsValidMoveRulesEngine.rules.get(2) instanceof IsJumpingRule);
        assertTrue(kingIsValidMoveRulesEngine.rules.get(3) instanceof IsMovingIntoCheckRule);
        assertTrue(kingIsValidMoveRulesEngine.rules.get(4) instanceof KingIsValidBasicMoveRule);
        assertTrue(kingIsValidMoveRulesEngine.rules.get(5) instanceof KingIsValidCastlingRule);
        assertTrue(kingIsValidMoveRulesEngine.rules.get(6) instanceof IsNotValidRule);
    }
}
