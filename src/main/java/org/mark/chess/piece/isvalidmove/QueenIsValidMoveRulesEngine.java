package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class QueenIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public QueenIsValidMoveRulesEngine() {
        rules.add(new HasEmptyParametersRule());
        rules.add(new IsFriendlyFireRule());
        rules.add(new IsJumpingRule());
        rules.add(new IsMovingIntoCheckRule());
        rules.add(new QueenIsValidBasicMoveRule());
        rules.add(new IsNotValidRule());
    }
}
