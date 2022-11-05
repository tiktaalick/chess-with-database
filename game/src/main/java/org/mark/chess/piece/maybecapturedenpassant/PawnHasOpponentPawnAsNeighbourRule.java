package org.mark.chess.piece.maybecapturedenpassant;

import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.rulesengine.Rule;
import org.mark.chess.piece.isvalidmove.PieceTypeSharedRules;

public class PawnHasOpponentPawnAsNeighbourRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean createResult() {
        return true;
    }

    @Override
    public boolean hasResult(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return !neighbourFieldsWithOpponentPawns(getGrid(), getTo(), getFrom().getPieceType().getColor()).isEmpty();
    }
}
