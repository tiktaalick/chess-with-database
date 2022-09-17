package org.mark.chess.rulesengine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.rulesengine.rule.piecetype.HasEmptyParametersRule;
import org.mark.chess.rulesengine.rule.piecetype.IsFriendlyFireRule;
import org.mark.chess.rulesengine.rule.piecetype.IsJumpingRule;
import org.mark.chess.rulesengine.rule.piecetype.IsMovingIntoCheckRule;
import org.mark.chess.rulesengine.rule.piecetype.IsNotValidRule;
import org.mark.chess.rulesengine.rule.piecetype.RookIsValidBasicMoveRule;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RookIsValidMoveRulesEngineTest {
    @InjectMocks
    private RookIsValidMoveRulesEngine rookIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertTrue(rookIsValidMoveRulesEngine.rules.get(0) instanceof HasEmptyParametersRule);
        assertTrue(rookIsValidMoveRulesEngine.rules.get(1) instanceof IsFriendlyFireRule);
        assertTrue(rookIsValidMoveRulesEngine.rules.get(2) instanceof IsJumpingRule);
        assertTrue(rookIsValidMoveRulesEngine.rules.get(3) instanceof IsMovingIntoCheckRule);
        assertTrue(rookIsValidMoveRulesEngine.rules.get(4) instanceof RookIsValidBasicMoveRule);
        assertTrue(rookIsValidMoveRulesEngine.rules.get(5) instanceof IsNotValidRule);
    }
}