package org.mark.chess.piece.king.isvalidmove;

import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules;
import org.mark.chess.rulesengine.Rule;

import java.util.Arrays;

public class KingIsValidBasicMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean getResult() {
        return true;
    }

    @Override
    public boolean isApplicable(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return Arrays.asList(ZERO_STEPS, ONE_STEP).contains(getAbsoluteHorizontalMove()) && Arrays.asList(ZERO_STEPS, ONE_STEP).contains(
                getAbsoluteVerticalMove()) && !(getAbsoluteHorizontalMove() == ZERO_STEPS && getAbsoluteVerticalMove() == ZERO_STEPS);
    }
}
