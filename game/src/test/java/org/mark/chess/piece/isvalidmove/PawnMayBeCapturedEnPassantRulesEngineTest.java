package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.maybecapturedenpassant.PawnMayBeCapturedEnPassantRulesEngine;
import org.mark.chess.piece.maybecapturedenpassant.PawnHasOpponentPawnAsNeighbourRule;
import org.mark.chess.piece.maybecapturedenpassant.PawnIsNotValidBaselineMoveRule;
import org.mark.chess.piece.isvalidmove.IsNotValidRule;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PawnMayBeCapturedEnPassantRulesEngineTest {
    @InjectMocks
    private PawnMayBeCapturedEnPassantRulesEngine pawnMayBeCapturedEnPassantRulesEngine;

    @Test
    void testRules() {
        assertTrue(pawnMayBeCapturedEnPassantRulesEngine.getRules().get(0) instanceof PawnIsNotValidBaselineMoveRule);
        assertTrue(pawnMayBeCapturedEnPassantRulesEngine.getRules().get(1) instanceof PawnHasOpponentPawnAsNeighbourRule);
        assertTrue(pawnMayBeCapturedEnPassantRulesEngine.getRules().get(2) instanceof IsNotValidRule);
    }
}