package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.Rule;

public class QueenIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean createResult() {
        return true;
    }

    @Override
    public boolean hasResult(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return isHorizontalMove() || isVerticalMove() || isDiagonalMove();
    }
}
