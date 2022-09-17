package org.mark.chess.rulesengine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.rulesengine.rule.piecetype.HasEmptyParametersRule;
import org.mark.chess.rulesengine.rule.piecetype.IsFriendlyFireRule;
import org.mark.chess.rulesengine.rule.piecetype.IsMovingIntoCheckRule;
import org.mark.chess.rulesengine.rule.piecetype.IsNotValidRule;
import org.mark.chess.rulesengine.rule.piecetype.KnightIsValidBasicMoveRule;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class KnightIsValidMoveRulesEngineTest {
    @InjectMocks
    private KnightIsValidMoveRulesEngine knightIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertTrue(knightIsValidMoveRulesEngine.rules.get(0) instanceof HasEmptyParametersRule);
        assertTrue(knightIsValidMoveRulesEngine.rules.get(1) instanceof IsFriendlyFireRule);
        assertTrue(knightIsValidMoveRulesEngine.rules.get(2) instanceof IsMovingIntoCheckRule);
        assertTrue(knightIsValidMoveRulesEngine.rules.get(3) instanceof KnightIsValidBasicMoveRule);
        assertTrue(knightIsValidMoveRulesEngine.rules.get(4) instanceof IsNotValidRule);
    }
}