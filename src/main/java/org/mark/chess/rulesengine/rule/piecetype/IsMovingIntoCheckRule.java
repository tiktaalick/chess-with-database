package org.mark.chess.rulesengine.rule.piecetype;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class IsMovingIntoCheckRule extends PieceTypeHelper implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return this.isValidMove;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        this.isValidMove = !isValidMoveParameter
                .getCheckLogic()
                .isMovingIntoCheck(isValidMoveParameter.getGrid(),
                        isValidMoveParameter.getFrom(),
                        isValidMoveParameter.getTo(),
                        isValidMoveParameter.isOpponent(),
                        isValidMoveParameter.getGridLogic());
        return !this.isValidMove;
    }
}
