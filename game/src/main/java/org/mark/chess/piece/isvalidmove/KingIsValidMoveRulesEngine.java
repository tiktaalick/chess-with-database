package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class KingIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    /**
     * Initializes the rules for this engine.
     */
    public KingIsValidMoveRulesEngine() {
        addRule(new HasEmptyParametersRule());
        addRule(new IsFriendlyFireRule());
        addRule(new IsJumpingRule());
        addRule(new IsMovingIntoCheckRule());
        addRule(new KingIsValidBasicMoveRule());
        addRule(new KingIsValidCastlingRule());
        addRule(new IsNotValidRule());
    }
}
