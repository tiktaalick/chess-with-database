package org.mark.chess.rulesengine.rule.piecetype;

import org.mark.chess.model.Field;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class KnightIsValidBasicMoveRule extends PieceTypeHelper implements Rule<IsValidMoveParameter, Boolean> {
    private static final int ONE_STEP  = 1;
    private static final int TWO_STEPS = 2;

    @Override
    public Boolean create() {
        return this.isValidMove;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        this.isValidMove = isValidBasicMove(isValidMoveParameter.getFrom(), isValidMoveParameter.getTo());
        return this.isValidMove;
    }

    private boolean isValidBasicMove(Field from, Field to) {
        int horizontalMove = getAbsoluteHorizontalMove(from, to);
        int verticalMove = getAbsoluteVerticalMove(from, to);
        return (horizontalMove == ONE_STEP && verticalMove == TWO_STEPS) || (horizontalMove == TWO_STEPS && verticalMove == ONE_STEP);
    }
}
