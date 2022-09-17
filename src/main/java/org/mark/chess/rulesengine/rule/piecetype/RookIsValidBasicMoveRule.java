package org.mark.chess.rulesengine.rule.piecetype;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class RookIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {
    @Override
    public Boolean create() {
        return isValidMove();
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        setValidMove((getAbsoluteHorizontalMove() != 0 && getAbsoluteVerticalMove() == 0) ||
                (getAbsoluteHorizontalMove() == 0 && getAbsoluteVerticalMove() != 0));

        return isValidMove();
    }
}
