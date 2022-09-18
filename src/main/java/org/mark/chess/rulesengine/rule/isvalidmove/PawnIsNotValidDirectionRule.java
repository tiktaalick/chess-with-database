package org.mark.chess.rulesengine.rule.isvalidmove;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class PawnIsNotValidDirectionRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {
    @Override
    public Boolean create() {
        return false;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return !isValidDirection(getFrom(), getTo());
    }

    private static boolean isValidDirection(Field from, Field to) {
        return Integer.signum(to.getCoordinates().getY() - from.getCoordinates().getY()) ==
                (from.getPiece().getColor() == Color.WHITE
                        ? 1
                        : -1);
    }
}
