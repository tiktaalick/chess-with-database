package org.mark.chess.piece.pawn.maybecapturedenpassant;

import org.mark.chess.piece.general.isvalidmove.IsNotValidRule;
import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.rulesengine.RulesEngine;

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
