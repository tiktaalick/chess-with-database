package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class KnightIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public KnightIsValidMoveRulesEngine() {
        rules.add(new HasEmptyParametersRule());
        rules.add(new IsFriendlyFireRule());
        rules.add(new IsMovingIntoCheckRule());
        rules.add(new KnightIsValidBasicMoveRule());
        rules.add(new IsNotValidRule());
    }
}
