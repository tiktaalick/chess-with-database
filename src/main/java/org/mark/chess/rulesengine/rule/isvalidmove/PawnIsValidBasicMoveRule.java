package org.mark.chess.rulesengine.rule.isvalidmove;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class PawnIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {
    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return !isCaptureMove(getFrom(), getTo()) &&
                getAbsoluteHorizontalMove(getFrom(), getTo()) == 0 &&
                getAbsoluteVerticalMove(getFrom(), getTo()) == 1;
    }
}
