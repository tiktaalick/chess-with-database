package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.Rule;

public class KnightIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean createResult() {
        return true;
    }

    @Override
    public boolean hasResult(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return (getAbsoluteHorizontalMove() == ONE_STEP && getAbsoluteVerticalMove() == TWO_STEPS) ||
                (getAbsoluteHorizontalMove() == TWO_STEPS && getAbsoluteVerticalMove() == ONE_STEP);
    }
}
