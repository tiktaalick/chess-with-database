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

        return (this.isValidMoveParameter.getAbsoluteHorizontalMove() == ONE_STEP &&
                this.isValidMoveParameter.getAbsoluteVerticalMove() == TWO_STEPS) ||
                (this.isValidMoveParameter.getAbsoluteHorizontalMove() == TWO_STEPS &&
                        this.isValidMoveParameter.getAbsoluteVerticalMove() == ONE_STEP);
    }
}
