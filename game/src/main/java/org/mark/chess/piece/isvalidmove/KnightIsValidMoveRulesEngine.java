package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class KnightIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    /**
     * Initializes the rules for this engine.
     */
    public KnightIsValidMoveRulesEngine() {
        addRule(new HasEmptyParametersRule());
        addRule(new IsFriendlyFireRule());
        addRule(new IsMovingIntoCheckRule());
        addRule(new KnightIsValidBasicMoveRule());
        addRule(new IsNotValidRule());
    }
}
