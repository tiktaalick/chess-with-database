package org.mark.chess.piece.pawn.isvalidmove;

import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules;
import org.mark.chess.rulesengine.Rule;

public class PawnIsValidBaselineMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean getResult() {
        return true;
    }

    @Override
    public boolean isApplicable(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return pawnIsValidBaselineMove();
    }
}
