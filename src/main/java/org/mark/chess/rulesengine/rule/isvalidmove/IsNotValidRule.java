package org.mark.chess.rulesengine.rule.isvalidmove;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class IsNotValidRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return false;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        return true;
    }
}
