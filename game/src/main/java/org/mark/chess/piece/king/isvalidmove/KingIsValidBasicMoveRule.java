package org.mark.chess.piece.king.isvalidmove;

import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules;
import org.mark.chess.rulesengine.Rule;

import java.util.Arrays;

public class KingIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

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

        return Arrays.asList(ZERO_STEPS, ONE_STEP).contains(isValidMoveParameter.getAbsoluteHorizontalMove()) &&
                Arrays.asList(ZERO_STEPS, ONE_STEP).contains(isValidMoveParameter.getAbsoluteVerticalMove()) &&
                !(isValidMoveParameter.getAbsoluteHorizontalMove() == ZERO_STEPS && isValidMoveParameter.getAbsoluteVerticalMove() == ZERO_STEPS);
    }
}
