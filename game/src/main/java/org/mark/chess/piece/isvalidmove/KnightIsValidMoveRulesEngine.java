package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class KnightIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public KnightIsValidMoveRulesEngine() {
        getRules().add(new HasEmptyParametersRule());
        getRules().add(new IsFriendlyFireRule());
        getRules().add(new IsMovingIntoCheckRule());
        getRules().add(new KnightIsValidBasicMoveRule());
        getRules().add(new IsNotValidRule());
    }
}
