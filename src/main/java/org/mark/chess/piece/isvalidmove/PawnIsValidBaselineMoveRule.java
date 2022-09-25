package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.Rule;

public class PawnIsValidBaselineMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean isApplicable(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return pawnIsValidBaselineMove();
    }
}
