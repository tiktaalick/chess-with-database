package org.mark.chess.rulesengine.rule.piecetype;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class BishopIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {
    @Override
    public Boolean create() {
        return this.isValidMove();
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        this.setParameter(isValidMoveParameter);

        this.setValidMove(this.getAbsoluteHorizontalMove() == this.getAbsoluteVerticalMove() && this.getAbsoluteHorizontalMove() != 0);

        return this.isValidMove();
    }
}
