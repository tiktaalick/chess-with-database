package org.mark.chess.piece.maybecapturedenpassant;

import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.rulesengine.RulesEngine;
import org.mark.chess.piece.isvalidmove.IsNotValidRule;

public final class PawnMayBeCapturedEnPassantRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public PawnMayBeCapturedEnPassantRulesEngine() {
        rules.add(new PawnIsNotValidBaselineMoveRule());
        rules.add(new PawnHasOpponentPawnAsNeighbourRule());
        rules.add(new IsNotValidRule());
    }
}
