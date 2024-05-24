package org.mark.chess.piece.knight.isvalidmove;

import org.mark.chess.piece.general.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.piece.general.isvalidmove.HasNotPassedThePreliminaryRoundsRule;
import org.mark.chess.piece.general.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.general.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.general.isvalidmove.IsNotAValidMoveRule;
import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.rulesengine.RulesEngine;

public final class KnightIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    /**
     * Initializes the rules for this engine.
     */
    public KnightIsValidMoveRulesEngine() {
        addRule(new HasEmptyParametersRule());
        addRule(new HasNotPassedThePreliminaryRoundsRule());
        addRule(new IsFriendlyFireRule());
        addRule(new IsMovingIntoCheckRule());
        addRule(new KnightIsValidBasicMoveRule());
        addRule(new IsNotAValidMoveRule());
    }
}
