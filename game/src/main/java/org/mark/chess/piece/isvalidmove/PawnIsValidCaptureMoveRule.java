package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.Rule;

public class PawnIsValidCaptureMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean createResult() {
        return true;
    }

    @Override
    public boolean hasResult(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return isCaptureMove(getFrom(), getTo()) &&
                getAbsoluteHorizontalMove(getFrom(), getTo()) == ONE_STEP &&
                getAbsoluteVerticalMove(getFrom(), getTo()) == ONE_STEP;
    }
}
