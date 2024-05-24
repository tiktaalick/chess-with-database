package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.general.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.piece.general.isvalidmove.HasPassedThePreliminaryRoundsRule;
import org.mark.chess.piece.general.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.general.isvalidmove.IsJumpingRule;
import org.mark.chess.piece.general.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.general.isvalidmove.IsNotAValidMoveRule;
import org.mark.chess.piece.pawn.isvalidmove.PawnIsNotValidDirectionRule;
import org.mark.chess.piece.pawn.isvalidmove.PawnIsValidBaselineMoveRule;
import org.mark.chess.piece.pawn.isvalidmove.PawnIsValidBasicMoveRule;
import org.mark.chess.piece.pawn.isvalidmove.PawnIsValidCaptureMoveRule;
import org.mark.chess.piece.pawn.isvalidmove.PawnIsValidEnPassantMoveRule;
import org.mark.chess.piece.pawn.isvalidmove.PawnIsValidMoveRulesEngine;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(MockitoExtension.class)
class PawnIsValidMoveRulesEngineTest {

    @InjectMocks
    private PawnIsValidMoveRulesEngine pawnIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertInstanceOf(HasEmptyParametersRule.class, pawnIsValidMoveRulesEngine.getRules().get(0));
        assertInstanceOf(HasPassedThePreliminaryRoundsRule.class, pawnIsValidMoveRulesEngine.getRules().get(1));
        assertInstanceOf(IsFriendlyFireRule.class, pawnIsValidMoveRulesEngine.getRules().get(2));
        assertInstanceOf(IsJumpingRule.class, pawnIsValidMoveRulesEngine.getRules().get(3));
        assertInstanceOf(IsMovingIntoCheckRule.class, pawnIsValidMoveRulesEngine.getRules().get(4));
        assertInstanceOf(PawnIsNotValidDirectionRule.class, pawnIsValidMoveRulesEngine.getRules().get(5));
        assertInstanceOf(PawnIsValidBasicMoveRule.class, pawnIsValidMoveRulesEngine.getRules().get(6));
        assertInstanceOf(PawnIsValidBaselineMoveRule.class, pawnIsValidMoveRulesEngine.getRules().get(7));
        assertInstanceOf(PawnIsValidCaptureMoveRule.class, pawnIsValidMoveRulesEngine.getRules().get(8));
        assertInstanceOf(PawnIsValidEnPassantMoveRule.class, pawnIsValidMoveRulesEngine.getRules().get(9));
        assertInstanceOf(IsNotAValidMoveRule.class, pawnIsValidMoveRulesEngine.getRules().get(10));
    }
}
