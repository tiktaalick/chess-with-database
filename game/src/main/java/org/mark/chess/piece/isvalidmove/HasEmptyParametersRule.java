package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.Rule;

public class HasEmptyParametersRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return false;
    }

    @Override
    public boolean isApplicable(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return isValidMoveParameter == null || getGrid() == null || getFrom() == null || getTo() == null;
    }
}
