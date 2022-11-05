package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class BishopIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    /**
     * Initializes the rules for this engine.
     */
    public BishopIsValidMoveRulesEngine() {
        addRule(new HasEmptyParametersRule());
        addRule(new IsFriendlyFireRule());
        addRule(new IsJumpingRule());
        addRule(new IsMovingIntoCheckRule());
        addRule(new BishopIsValidBasicMoveRule());
        addRule(new IsNotValidRule());
    }
}
