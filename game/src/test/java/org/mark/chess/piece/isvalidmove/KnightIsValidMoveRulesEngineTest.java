package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.piece.general.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.piece.general.isvalidmove.HasPassedThePreliminaryRoundsRule;
import org.mark.chess.piece.general.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.general.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.general.isvalidmove.IsNotAValidMoveRule;
import org.mark.chess.piece.knight.isvalidmove.KnightIsValidBasicMoveRule;
import org.mark.chess.piece.knight.isvalidmove.KnightIsValidMoveRulesEngine;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(MockitoExtension.class)
class KnightIsValidMoveRulesEngineTest {

    @InjectMocks
    private KnightIsValidMoveRulesEngine knightIsValidMoveRulesEngine;

    @Test
    void testRules() {
        assertInstanceOf(HasEmptyParametersRule.class, knightIsValidMoveRulesEngine.getRules().get(0));
        assertInstanceOf(HasPassedThePreliminaryRoundsRule.class, knightIsValidMoveRulesEngine.getRules().get(1));
        assertInstanceOf(IsFriendlyFireRule.class, knightIsValidMoveRulesEngine.getRules().get(2));
        assertInstanceOf(IsMovingIntoCheckRule.class, knightIsValidMoveRulesEngine.getRules().get(3));
        assertInstanceOf(KnightIsValidBasicMoveRule.class, knightIsValidMoveRulesEngine.getRules().get(4));
        assertInstanceOf(IsNotAValidMoveRule.class, knightIsValidMoveRulesEngine.getRules().get(5));
    }
}
