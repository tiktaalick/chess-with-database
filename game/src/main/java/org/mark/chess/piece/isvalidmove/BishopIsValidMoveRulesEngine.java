package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class BishopIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public BishopIsValidMoveRulesEngine() {
        getRules().add(new HasEmptyParametersRule());
        getRules().add(new IsFriendlyFireRule());
        getRules().add(new IsJumpingRule());
        getRules().add(new IsMovingIntoCheckRule());
        getRules().add(new BishopIsValidBasicMoveRule());
        getRules().add(new IsNotValidRule());
    }
}
