package org.mark.chess.rulesengine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.rulesengine.rule.piecetype.HasEmptyParametersRule;
import org.mark.chess.rulesengine.rule.piecetype.IsFriendlyFireRule;
import org.mark.chess.rulesengine.rule.piecetype.IsJumpingRule;
import org.mark.chess.rulesengine.rule.piecetype.IsMovingIntoCheckRule;
import org.mark.chess.rulesengine.rule.piecetype.IsNotValidRule;
import org.mark.chess.rulesengine.rule.piecetype.PawnIsNotValidDirectionRule;
import org.mark.chess.rulesengine.rule.piecetype.PawnIsValidBaselineMoveRule;
import org.mark.chess.rulesengine.rule.piecetype.PawnIsValidBasicMoveRule;
import org.mark.chess.rulesengine.rule.piecetype.PawnIsValidCaptureMoveRule;
import org.mark.chess.rulesengine.rule.piecetype.PawnIsValidEnPassantMoveRule;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PawnIsValidMoveRulesEngineTest {
    @InjectMocks
    private PawnIsValidMoveRulesEngine pawnIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertTrue(pawnIsValidMoveRulesEngine.rules.get(0) instanceof HasEmptyParametersRule);
        assertTrue(pawnIsValidMoveRulesEngine.rules.get(1) instanceof IsFriendlyFireRule);
        assertTrue(pawnIsValidMoveRulesEngine.rules.get(2) instanceof IsJumpingRule);
        assertTrue(pawnIsValidMoveRulesEngine.rules.get(3) instanceof IsMovingIntoCheckRule);
        assertTrue(pawnIsValidMoveRulesEngine.rules.get(4) instanceof PawnIsNotValidDirectionRule);
        assertTrue(pawnIsValidMoveRulesEngine.rules.get(5) instanceof PawnIsValidBasicMoveRule);
        assertTrue(pawnIsValidMoveRulesEngine.rules.get(6) instanceof PawnIsValidBaselineMoveRule);
        assertTrue(pawnIsValidMoveRulesEngine.rules.get(7) instanceof PawnIsValidCaptureMoveRule);
        assertTrue(pawnIsValidMoveRulesEngine.rules.get(8) instanceof PawnIsValidEnPassantMoveRule);
        assertTrue(pawnIsValidMoveRulesEngine.rules.get(9) instanceof IsNotValidRule);
    }
}
