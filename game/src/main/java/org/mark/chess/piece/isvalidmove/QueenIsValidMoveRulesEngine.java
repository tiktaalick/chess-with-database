package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class QueenIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public QueenIsValidMoveRulesEngine() {
        getRules().add(new HasEmptyParametersRule());
        getRules().add(new IsFriendlyFireRule());
        getRules().add(new IsJumpingRule());
        getRules().add(new IsMovingIntoCheckRule());
        getRules().add(new QueenIsValidBasicMoveRule());
        getRules().add(new IsNotValidRule());
    }
}
