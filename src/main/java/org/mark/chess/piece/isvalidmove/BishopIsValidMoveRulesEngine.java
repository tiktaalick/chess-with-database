package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class BishopIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public BishopIsValidMoveRulesEngine() {
        rules.add(new HasEmptyParametersRule());
        rules.add(new IsFriendlyFireRule());
        rules.add(new IsJumpingRule());
        rules.add(new IsMovingIntoCheckRule());
        rules.add(new BishopIsValidBasicMoveRule());
        rules.add(new IsNotValidRule());
    }
}
