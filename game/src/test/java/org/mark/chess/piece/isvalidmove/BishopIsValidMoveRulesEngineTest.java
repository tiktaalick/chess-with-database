package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.bishop.isvalidmove.BishopIsValidBasicMoveRule;
import org.mark.chess.piece.bishop.isvalidmove.BishopIsValidMoveRulesEngine;
import org.mark.chess.piece.general.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.piece.general.isvalidmove.HasPassedThePreliminaryRoundsRule;
import org.mark.chess.piece.general.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.general.isvalidmove.IsJumpingRule;
import org.mark.chess.piece.general.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.general.isvalidmove.IsNotAValidMoveRule;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(MockitoExtension.class)
class BishopIsValidMoveRulesEngineTest {

    @InjectMocks
    private BishopIsValidMoveRulesEngine bishopIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertInstanceOf(HasEmptyParametersRule.class, bishopIsValidMoveRulesEngine.getRules().get(0));
        assertInstanceOf(HasPassedThePreliminaryRoundsRule.class, bishopIsValidMoveRulesEngine.getRules().get(1));
        assertInstanceOf(IsFriendlyFireRule.class, bishopIsValidMoveRulesEngine.getRules().get(2));
        assertInstanceOf(IsJumpingRule.class, bishopIsValidMoveRulesEngine.getRules().get(3));
        assertInstanceOf(IsMovingIntoCheckRule.class, bishopIsValidMoveRulesEngine.getRules().get(4));
        assertInstanceOf(BishopIsValidBasicMoveRule.class, bishopIsValidMoveRulesEngine.getRules().get(5));
        assertInstanceOf(IsNotAValidMoveRule.class, bishopIsValidMoveRulesEngine.getRules().get(6));
    }
}
