package org.mark.chess.piece.general.isvalidmove;

import org.mark.chess.rulesengine.Rule;

public class IsMovingIntoCheckRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean getResult() {
        return false;
    }

    @Override
    public boolean isApplicable(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return getFrom().isMovingIntoCheck(getGrid(), getTo(), isOpponent());
    }
}
