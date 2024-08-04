package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.general.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.piece.general.isvalidmove.HasNotPassedThePreliminaryRoundsRule;
import org.mark.chess.piece.general.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.general.isvalidmove.IsJumpingRule;
import org.mark.chess.piece.general.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.general.isvalidmove.IsNotAValidMoveRule;
import org.mark.chess.piece.rook.isvalidmove.RookIsValidBasicMoveRule;
import org.mark.chess.piece.rook.isvalidmove.RookIsValidMoveRulesEngine;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(MockitoExtension.class)
class RookIsValidMoveRulesEngineTest {

    @InjectMocks
    private RookIsValidMoveRulesEngine rookIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertInstanceOf(HasEmptyParametersRule.class, rookIsValidMoveRulesEngine.getRules().get(0));
        assertInstanceOf(HasNotPassedThePreliminaryRoundsRule.class, rookIsValidMoveRulesEngine.getRules().get(1));
        assertInstanceOf(IsFriendlyFireRule.class, rookIsValidMoveRulesEngine.getRules().get(2));
        assertInstanceOf(IsJumpingRule.class, rookIsValidMoveRulesEngine.getRules().get(3));
        assertInstanceOf(IsMovingIntoCheckRule.class, rookIsValidMoveRulesEngine.getRules().get(4));
        assertInstanceOf(RookIsValidBasicMoveRule.class, rookIsValidMoveRulesEngine.getRules().get(5));
        assertInstanceOf(IsNotAValidMoveRule.class, rookIsValidMoveRulesEngine.getRules().get(6));
    }
}
