package org.mark.chess.rulesengine.rule.piecetype;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class IsNotValidRule extends PieceTypeHelper implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return false;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        return true;
    }
}
