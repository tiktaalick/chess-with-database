package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class RookIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public RookIsValidMoveRulesEngine() {
        rules.add(new HasEmptyParametersRule());
        rules.add(new IsFriendlyFireRule());
        rules.add(new IsJumpingRule());
        rules.add(new IsMovingIntoCheckRule());
        rules.add(new RookIsValidBasicMoveRule());
        rules.add(new IsNotValidRule());
    }
}
