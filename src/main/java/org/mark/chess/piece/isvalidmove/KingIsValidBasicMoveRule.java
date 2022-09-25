package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.Rule;

import java.util.Arrays;

public class KingIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean isApplicable(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return Arrays.asList(ZERO_STEPS, ONE_STEP).contains(getAbsoluteHorizontalMove()) &&
                Arrays.asList(ZERO_STEPS, ONE_STEP).contains(getAbsoluteVerticalMove()) &&
                !(getAbsoluteHorizontalMove() == ZERO_STEPS && getAbsoluteVerticalMove() == ZERO_STEPS);
    }
}
