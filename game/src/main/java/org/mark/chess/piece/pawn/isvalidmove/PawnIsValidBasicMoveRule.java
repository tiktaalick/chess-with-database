package org.mark.chess.piece.pawn.isvalidmove;

import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules;
import org.mark.chess.rulesengine.Rule;

public class PawnIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    private IsValidMoveParameter isValidMoveParameter;

    @Override
    public String getContext() {
        return super.getContext(isValidMoveParameter);
    }

    @Override
    public Boolean getResult() {
        return true;
    }

    @Override
    public boolean stopProcessingfurtherRulesAndGetResultNow(IsValidMoveParameter isValidMoveParameter) {
        this.isValidMoveParameter = isValidMoveParameter;

        return !isCaptureMove(isValidMoveParameter.getFrom(), isValidMoveParameter.getTo()) &&
                getAbsoluteHorizontalMove(isValidMoveParameter.getFrom(), isValidMoveParameter.getTo()) == ZERO_STEPS &&
                getAbsoluteVerticalMove(isValidMoveParameter.getFrom(), isValidMoveParameter.getTo()) == ONE_STEP;
    }
}
