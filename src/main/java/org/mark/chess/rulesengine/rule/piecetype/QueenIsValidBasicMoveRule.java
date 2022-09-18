package org.mark.chess.rulesengine.rule.piecetype;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class QueenIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {
    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return (getAbsoluteHorizontalMove() != 0 && getAbsoluteVerticalMove() == 0) ||
                (getAbsoluteHorizontalMove() == 0 && getAbsoluteVerticalMove() != 0) ||
                (getAbsoluteHorizontalMove() != 0 && getAbsoluteHorizontalMove() == getAbsoluteVerticalMove());
    }
}
