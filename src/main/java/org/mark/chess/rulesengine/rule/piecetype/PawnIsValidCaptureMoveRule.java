package org.mark.chess.rulesengine.rule.piecetype;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class PawnIsValidCaptureMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {
    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return isCaptureMove(getFrom(), getTo()) &&
                getAbsoluteHorizontalMove(getFrom(), getTo()) == 1 &&
                getAbsoluteVerticalMove(getFrom(), getTo()) == 1;
    }
}
