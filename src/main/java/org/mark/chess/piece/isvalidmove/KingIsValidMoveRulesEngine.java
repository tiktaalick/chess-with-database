package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class KingIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public KingIsValidMoveRulesEngine() {
        rules.add(new HasEmptyParametersRule());
        rules.add(new IsFriendlyFireRule());
        rules.add(new IsJumpingRule());
        rules.add(new IsMovingIntoCheckRule());
        rules.add(new KingIsValidBasicMoveRule());
        rules.add(new KingIsValidCastlingRule());
        rules.add(new IsNotValidRule());
    }
}
