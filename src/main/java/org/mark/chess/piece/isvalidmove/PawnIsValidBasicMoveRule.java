package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.Rule;

public class PawnIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean isApplicable(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return !isCaptureMove(getFrom(), getTo()) &&
                getAbsoluteHorizontalMove(getFrom(), getTo()) == ZERO_STEPS &&
                getAbsoluteVerticalMove(getFrom(), getTo()) == ONE_STEP;
    }
}
