package org.mark.chess.rulesengine.rule.maybecapturedenpassant;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;
import org.mark.chess.rulesengine.rule.isvalidmove.PieceTypeSharedRules;

public class PawnIsNotValidBaselineMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return false;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        this.setParameter(isValidMoveParameter);

        return !pawnIsValidBaselineMove();
    }
}
