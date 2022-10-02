package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.piece.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.isvalidmove.IsNotValidRule;
import org.mark.chess.piece.isvalidmove.KnightIsValidBasicMoveRule;
import org.mark.chess.piece.isvalidmove.KnightIsValidMoveRulesEngine;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class KnightIsValidMoveRulesEngineTest {
    @InjectMocks
    private KnightIsValidMoveRulesEngine knightIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertTrue(knightIsValidMoveRulesEngine.getRules().get(0) instanceof HasEmptyParametersRule);
        assertTrue(knightIsValidMoveRulesEngine.getRules().get(1) instanceof IsFriendlyFireRule);
        assertTrue(knightIsValidMoveRulesEngine.getRules().get(2) instanceof IsMovingIntoCheckRule);
        assertTrue(knightIsValidMoveRulesEngine.getRules().get(3) instanceof KnightIsValidBasicMoveRule);
        assertTrue(knightIsValidMoveRulesEngine.getRules().get(4) instanceof IsNotValidRule);
    }
}