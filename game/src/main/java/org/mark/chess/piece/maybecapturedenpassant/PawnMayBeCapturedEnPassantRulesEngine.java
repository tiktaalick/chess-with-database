package org.mark.chess.piece.maybecapturedenpassant;

import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.rulesengine.RulesEngine;
import org.mark.chess.piece.isvalidmove.IsNotValidRule;

public final class PawnMayBeCapturedEnPassantRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    /**
     * Initializes the rules for this engine.
     */
    public PawnMayBeCapturedEnPassantRulesEngine() {
        addRule(new PawnIsNotValidBaselineMoveRule());
        addRule(new PawnHasOpponentPawnAsNeighbourRule());
        addRule(new IsNotValidRule());
    }
}
