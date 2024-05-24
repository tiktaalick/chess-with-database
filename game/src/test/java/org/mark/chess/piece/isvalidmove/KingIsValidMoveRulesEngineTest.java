package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.general.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.piece.general.isvalidmove.HasNotPassedThePreliminaryRoundsRule;
import org.mark.chess.piece.general.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.general.isvalidmove.IsJumpingRule;
import org.mark.chess.piece.general.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.general.isvalidmove.IsNotAValidMoveRule;
import org.mark.chess.piece.king.isvalidmove.KingIsValidBasicMoveRule;
import org.mark.chess.piece.king.isvalidmove.KingIsValidCastlingRule;
import org.mark.chess.piece.king.isvalidmove.KingIsValidMoveRulesEngine;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(MockitoExtension.class)
class KingIsValidMoveRulesEngineTest {

    @InjectMocks
    private KingIsValidMoveRulesEngine kingIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertInstanceOf(HasEmptyParametersRule.class, kingIsValidMoveRulesEngine.getRules().get(0));
        assertInstanceOf(HasNotPassedThePreliminaryRoundsRule.class, kingIsValidMoveRulesEngine.getRules().get(1));
        assertInstanceOf(IsFriendlyFireRule.class, kingIsValidMoveRulesEngine.getRules().get(2));
        assertInstanceOf(IsJumpingRule.class, kingIsValidMoveRulesEngine.getRules().get(3));
        assertInstanceOf(IsMovingIntoCheckRule.class, kingIsValidMoveRulesEngine.getRules().get(4));
        assertInstanceOf(KingIsValidBasicMoveRule.class, kingIsValidMoveRulesEngine.getRules().get(5));
        assertInstanceOf(KingIsValidCastlingRule.class, kingIsValidMoveRulesEngine.getRules().get(6));
        assertInstanceOf(IsNotAValidMoveRule.class, kingIsValidMoveRulesEngine.getRules().get(7));
    }
}
