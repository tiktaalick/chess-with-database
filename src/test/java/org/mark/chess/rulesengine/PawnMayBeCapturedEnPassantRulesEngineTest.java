package org.mark.chess.rulesengine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.rulesengine.rule.maybecapturedenpassant.PawnHasOpponentPawnAsNeighbourRule;
import org.mark.chess.rulesengine.rule.maybecapturedenpassant.PawnIsNotValidBaselineMoveRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsNotValidRule;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PawnMayBeCapturedEnPassantRulesEngineTest {
    @InjectMocks
    private PawnMayBeCapturedEnPassantRulesEngine pawnMayBeCapturedEnPassantRulesEngine;

    @Test
    void testRules() {
        assertTrue(pawnMayBeCapturedEnPassantRulesEngine.rules.get(0) instanceof PawnIsNotValidBaselineMoveRule);
        assertTrue(pawnMayBeCapturedEnPassantRulesEngine.rules.get(1) instanceof PawnHasOpponentPawnAsNeighbourRule);
        assertTrue(pawnMayBeCapturedEnPassantRulesEngine.rules.get(2) instanceof IsNotValidRule);
    }
}