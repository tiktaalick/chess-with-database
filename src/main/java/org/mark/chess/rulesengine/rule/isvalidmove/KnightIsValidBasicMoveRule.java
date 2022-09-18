package org.mark.chess.rulesengine.rule.isvalidmove;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class KnightIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {
    private static final int ONE_STEP  = 1;
    private static final int TWO_STEPS = 2;

    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return (getAbsoluteHorizontalMove() == ONE_STEP && getAbsoluteVerticalMove() == TWO_STEPS) ||
                (getAbsoluteHorizontalMove() == TWO_STEPS && getAbsoluteVerticalMove() == ONE_STEP);
    }
}
