package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class PawnIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    /**
     * Initializes the rules for this engine.
     */
    public PawnIsValidMoveRulesEngine() {
        addRule(new HasEmptyParametersRule());
        addRule(new IsFriendlyFireRule());
        addRule(new IsJumpingRule());
        addRule(new IsMovingIntoCheckRule());
        addRule(new PawnIsNotValidDirectionRule());
        addRule(new PawnIsValidBasicMoveRule());
        addRule(new PawnIsValidBaselineMoveRule());
        addRule(new PawnIsValidCaptureMoveRule());
        addRule(new PawnIsValidEnPassantMoveRule());
        addRule(new IsNotValidRule());
    }
}
