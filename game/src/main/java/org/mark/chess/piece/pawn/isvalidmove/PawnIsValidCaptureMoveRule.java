package org.mark.chess.piece.pawn.isvalidmove;

import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules;
import org.mark.chess.rulesengine.Rule;

public class PawnIsValidCaptureMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean getResult() {
        return true;
    }

    @Override
    public boolean stopProcessingfurtherRulesAndGetResultNow(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return isCaptureMove(getFrom(), getTo()) &&
                getAbsoluteHorizontalMove(getFrom(), getTo()) == ONE_STEP &&
                getAbsoluteVerticalMove(getFrom(), getTo()) == ONE_STEP;
    }
}
