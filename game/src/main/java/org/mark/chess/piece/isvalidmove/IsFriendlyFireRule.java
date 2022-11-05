package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.Rule;

public class IsFriendlyFireRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean createResult() {
        return false;
    }

    @Override
    public boolean hasResult(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return getTo().getPieceType() != null && getTo().getPieceType().getColor() == getFrom().getPieceType().getColor();
    }
}
