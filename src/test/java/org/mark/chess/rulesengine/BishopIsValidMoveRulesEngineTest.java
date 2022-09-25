package org.mark.chess.rulesengine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.isvalidmove.BishopIsValidMoveRulesEngine;
import org.mark.chess.piece.isvalidmove.BishopIsValidBasicMoveRule;
import org.mark.chess.piece.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.piece.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.isvalidmove.IsJumpingRule;
import org.mark.chess.piece.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.isvalidmove.IsNotValidRule;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BishopIsValidMoveRulesEngineTest {
    @InjectMocks
    private BishopIsValidMoveRulesEngine bishopIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertTrue(bishopIsValidMoveRulesEngine.rules.get(0) instanceof HasEmptyParametersRule);
        assertTrue(bishopIsValidMoveRulesEngine.rules.get(1) instanceof IsFriendlyFireRule);
        assertTrue(bishopIsValidMoveRulesEngine.rules.get(2) instanceof IsJumpingRule);
        assertTrue(bishopIsValidMoveRulesEngine.rules.get(3) instanceof IsMovingIntoCheckRule);
        assertTrue(bishopIsValidMoveRulesEngine.rules.get(4) instanceof BishopIsValidBasicMoveRule);
        assertTrue(bishopIsValidMoveRulesEngine.rules.get(5) instanceof IsNotValidRule);
    }
}