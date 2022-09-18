package org.mark.chess.rulesengine.rule.isvalidmove;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

import java.util.Arrays;

public class KingIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {
    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return Arrays.asList(0, 1).contains(getAbsoluteHorizontalMove()) &&
                Arrays.asList(0, 1).contains(getAbsoluteVerticalMove()) &&
                !(getAbsoluteHorizontalMove() == 0 && getAbsoluteVerticalMove() == 0);
    }
}
