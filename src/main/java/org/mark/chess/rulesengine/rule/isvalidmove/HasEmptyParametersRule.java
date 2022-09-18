package org.mark.chess.rulesengine.rule.isvalidmove;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class HasEmptyParametersRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return false;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return isValidMoveParameter == null ||
                getGrid() == null ||
                getFrom() == null ||
                getTo() == null ||
                getCheckLogic() == null ||
                getGridLogic() == null;
    }
}
