package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.Rule;

public class IsFriendlyFireRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return false;
    }

    @Override
    public boolean isApplicable(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return getTo().getPieceType() != null && getTo().getPieceType().getColor() == getFrom().getPieceType().getColor();
    }
}
