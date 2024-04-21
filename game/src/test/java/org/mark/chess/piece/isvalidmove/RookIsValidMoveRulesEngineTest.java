package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.general.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.piece.general.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.general.isvalidmove.IsJumpingRule;
import org.mark.chess.piece.general.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.general.isvalidmove.IsNotAValidMoveRule;
import org.mark.chess.piece.rook.isvalidmove.RookIsValidBasicMoveRule;
import org.mark.chess.piece.rook.isvalidmove.RookIsValidMoveRulesEngine;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RookIsValidMoveRulesEngineTest {

    @InjectMocks
    private RookIsValidMoveRulesEngine rookIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertTrue(rookIsValidMoveRulesEngine.getRules().get(0) instanceof HasEmptyParametersRule);
        assertTrue(rookIsValidMoveRulesEngine.getRules().get(1) instanceof IsFriendlyFireRule);
        assertTrue(rookIsValidMoveRulesEngine.getRules().get(2) instanceof IsJumpingRule);
        assertTrue(rookIsValidMoveRulesEngine.getRules().get(3) instanceof IsMovingIntoCheckRule);
        assertTrue(rookIsValidMoveRulesEngine.getRules().get(4) instanceof RookIsValidBasicMoveRule);
        assertTrue(rookIsValidMoveRulesEngine.getRules().get(5) instanceof IsNotAValidMoveRule);
    }
}
