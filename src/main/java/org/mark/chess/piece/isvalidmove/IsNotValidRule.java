package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.Rule;

public class IsNotValidRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return false;
    }

    @Override
    public boolean isApplicable(IsValidMoveParameter isValidMoveParameter) {
        return true;
    }
}
