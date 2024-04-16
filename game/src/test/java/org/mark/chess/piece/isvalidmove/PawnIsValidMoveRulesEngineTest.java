package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.general.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.piece.general.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.general.isvalidmove.IsJumpingRule;
import org.mark.chess.piece.general.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.general.isvalidmove.IsNotValidRule;
import org.mark.chess.piece.pawn.isvalidmove.PawnIsNotValidDirectionRule;
import org.mark.chess.piece.pawn.isvalidmove.PawnIsValidBaselineMoveRule;
import org.mark.chess.piece.pawn.isvalidmove.PawnIsValidBasicMoveRule;
import org.mark.chess.piece.pawn.isvalidmove.PawnIsValidCaptureMoveRule;
import org.mark.chess.piece.pawn.isvalidmove.PawnIsValidEnPassantMoveRule;
import org.mark.chess.piece.pawn.isvalidmove.PawnIsValidMoveRulesEngine;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PawnIsValidMoveRulesEngineTest {

    @InjectMocks
    private PawnIsValidMoveRulesEngine pawnIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertTrue(pawnIsValidMoveRulesEngine.getRules().get(0) instanceof HasEmptyParametersRule);
        assertTrue(pawnIsValidMoveRulesEngine.getRules().get(1) instanceof IsFriendlyFireRule);
        assertTrue(pawnIsValidMoveRulesEngine.getRules().get(2) instanceof IsJumpingRule);
        assertTrue(pawnIsValidMoveRulesEngine.getRules().get(3) instanceof IsMovingIntoCheckRule);
        assertTrue(pawnIsValidMoveRulesEngine.getRules().get(4) instanceof PawnIsNotValidDirectionRule);
        assertTrue(pawnIsValidMoveRulesEngine.getRules().get(5) instanceof PawnIsValidBasicMoveRule);
        assertTrue(pawnIsValidMoveRulesEngine.getRules().get(6) instanceof PawnIsValidBaselineMoveRule);
        assertTrue(pawnIsValidMoveRulesEngine.getRules().get(7) instanceof PawnIsValidCaptureMoveRule);
        assertTrue(pawnIsValidMoveRulesEngine.getRules().get(8) instanceof PawnIsValidEnPassantMoveRule);
        assertTrue(pawnIsValidMoveRulesEngine.getRules().get(9) instanceof IsNotValidRule);
    }
}
