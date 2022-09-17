package org.mark.chess.rulesengine.rule.piecetype;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class HasEmptyParametersRule extends PieceTypeHelper implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return this.isValidMove;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        this.isValidMove = !(isValidMoveParameter == null ||
                isValidMoveParameter.getGrid() == null ||
                isValidMoveParameter.getFrom() == null ||
                isValidMoveParameter.getTo() == null ||
                isValidMoveParameter.getCheckLogic() == null ||
                isValidMoveParameter.getGridLogic() == null);
        return !this.isValidMove;
    }
}
