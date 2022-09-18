package org.mark.chess.rulesengine;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.maybecapturedenpassant.PawnHasOpponentPawnAsNeighbourRule;
import org.mark.chess.rulesengine.rule.maybecapturedenpassant.PawnIsNotValidBaselineMoveRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsNotValidRule;

public final class PawnMayBeCapturedEnPassantRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public PawnMayBeCapturedEnPassantRulesEngine() {
        rules.add(new PawnIsNotValidBaselineMoveRule());
        rules.add(new PawnHasOpponentPawnAsNeighbourRule());
        rules.add(new IsNotValidRule());
    }
}
