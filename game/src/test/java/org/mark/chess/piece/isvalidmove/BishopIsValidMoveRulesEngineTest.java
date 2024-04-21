package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.bishop.isvalidmove.BishopIsValidBasicMoveRule;
import org.mark.chess.piece.bishop.isvalidmove.BishopIsValidMoveRulesEngine;
import org.mark.chess.piece.general.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.piece.general.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.general.isvalidmove.IsJumpingRule;
import org.mark.chess.piece.general.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.general.isvalidmove.IsNotAValidMoveRule;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BishopIsValidMoveRulesEngineTest {

    @InjectMocks
    private BishopIsValidMoveRulesEngine bishopIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertTrue(bishopIsValidMoveRulesEngine.getRules().get(0) instanceof HasEmptyParametersRule);
        assertTrue(bishopIsValidMoveRulesEngine.getRules().get(1) instanceof IsFriendlyFireRule);
        assertTrue(bishopIsValidMoveRulesEngine.getRules().get(2) instanceof IsJumpingRule);
        assertTrue(bishopIsValidMoveRulesEngine.getRules().get(3) instanceof IsMovingIntoCheckRule);
        assertTrue(bishopIsValidMoveRulesEngine.getRules().get(4) instanceof BishopIsValidBasicMoveRule);
        assertTrue(bishopIsValidMoveRulesEngine.getRules().get(5) instanceof IsNotAValidMoveRule);
    }
}
