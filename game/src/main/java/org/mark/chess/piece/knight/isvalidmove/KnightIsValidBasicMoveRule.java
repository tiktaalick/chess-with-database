package org.mark.chess.piece.knight.isvalidmove;

import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules;
import org.mark.chess.rulesengine.Rule;

public class KnightIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

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

        return (isValidMoveParameter.getAbsoluteHorizontalMove() == ONE_STEP && isValidMoveParameter.getAbsoluteVerticalMove() == TWO_STEPS) ||
                (isValidMoveParameter.getAbsoluteHorizontalMove() == TWO_STEPS && isValidMoveParameter.getAbsoluteVerticalMove() == ONE_STEP);
    }
}
