package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class KingIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public KingIsValidMoveRulesEngine() {
        getRules().add(new HasEmptyParametersRule());
        getRules().add(new IsFriendlyFireRule());
        getRules().add(new IsJumpingRule());
        getRules().add(new IsMovingIntoCheckRule());
        getRules().add(new KingIsValidBasicMoveRule());
        getRules().add(new KingIsValidCastlingRule());
        getRules().add(new IsNotValidRule());
    }
}
