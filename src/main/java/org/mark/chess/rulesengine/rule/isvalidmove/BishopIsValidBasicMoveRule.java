package org.mark.chess.rulesengine.rule.isvalidmove;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class BishopIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {
    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        this.setParameter(isValidMoveParameter);

        return this.getAbsoluteHorizontalMove() == this.getAbsoluteVerticalMove() && this.getAbsoluteHorizontalMove() != 0;
    }
}
