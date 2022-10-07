package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.isvalidmove.KingIsValidMoveRulesEngine;
import org.mark.chess.piece.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.piece.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.isvalidmove.IsJumpingRule;
import org.mark.chess.piece.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.isvalidmove.IsNotValidRule;
import org.mark.chess.piece.isvalidmove.KingIsValidBasicMoveRule;
import org.mark.chess.piece.isvalidmove.KingIsValidCastlingRule;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class KingIsValidMoveRulesEngineTest {
    @InjectMocks
    private KingIsValidMoveRulesEngine kingIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertTrue(kingIsValidMoveRulesEngine.getRules().get(0) instanceof HasEmptyParametersRule);
        assertTrue(kingIsValidMoveRulesEngine.getRules().get(1) instanceof IsFriendlyFireRule);
        assertTrue(kingIsValidMoveRulesEngine.getRules().get(2) instanceof IsJumpingRule);
        assertTrue(kingIsValidMoveRulesEngine.getRules().get(3) instanceof IsMovingIntoCheckRule);
        assertTrue(kingIsValidMoveRulesEngine.getRules().get(4) instanceof KingIsValidBasicMoveRule);
        assertTrue(kingIsValidMoveRulesEngine.getRules().get(5) instanceof KingIsValidCastlingRule);
        assertTrue(kingIsValidMoveRulesEngine.getRules().get(6) instanceof IsNotValidRule);
    }
}
