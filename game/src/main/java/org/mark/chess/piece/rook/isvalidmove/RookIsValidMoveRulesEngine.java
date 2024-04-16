package org.mark.chess.piece.rook.isvalidmove;

import org.mark.chess.piece.general.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.piece.general.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.piece.general.isvalidmove.IsJumpingRule;
import org.mark.chess.piece.general.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.piece.general.isvalidmove.IsNotValidRule;
import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.rulesengine.RulesEngine;

public final class RookIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    /**
     * Initializes the rules for this engine.
     */
    public RookIsValidMoveRulesEngine() {
        addRule(new HasEmptyParametersRule());
        addRule(new IsFriendlyFireRule());
        addRule(new IsJumpingRule());
        addRule(new IsMovingIntoCheckRule());
        addRule(new RookIsValidBasicMoveRule());
        addRule(new IsNotValidRule());
    }
}
