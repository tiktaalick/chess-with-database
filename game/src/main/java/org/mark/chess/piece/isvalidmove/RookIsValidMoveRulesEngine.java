package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class RookIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public RookIsValidMoveRulesEngine() {
        getRules().add(new HasEmptyParametersRule());
        getRules().add(new IsFriendlyFireRule());
        getRules().add(new IsJumpingRule());
        getRules().add(new IsMovingIntoCheckRule());
        getRules().add(new RookIsValidBasicMoveRule());
        getRules().add(new IsNotValidRule());
    }
}
