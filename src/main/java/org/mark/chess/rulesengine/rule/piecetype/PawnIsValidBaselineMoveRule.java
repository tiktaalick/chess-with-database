package org.mark.chess.rulesengine.rule.piecetype;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class PawnIsValidBaselineMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {
    private static final int TWO_STEPS_FORWARD_FROM_BASELINE = 2;

    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        this.setParameter(isValidMoveParameter);

        return !isCaptureMove(getFrom(), getTo()) &&
                getFrom().getPiece().getColor().getBaselinePawn() == getFrom().getCoordinates().getY() &&
                getAbsoluteHorizontalMove(getFrom(), getTo()) == 0 &&
                getAbsoluteVerticalMove(getFrom(), getTo()) == TWO_STEPS_FORWARD_FROM_BASELINE;
    }
}
