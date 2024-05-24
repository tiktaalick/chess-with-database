package org.mark.chess.piece.pawn.maybecapturedenpassant;

import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules;
import org.mark.chess.rulesengine.Rule;

public class PawnHasOpponentPawnAsNeighbourRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean getResult() {
        return true;
    }

    @Override
    public boolean stopProcessingfurtherRulesAndGetResultNow(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return !neighbourFieldsWithOpponentPawns(getChessboard(), getTo(), getFrom().getPieceType().getColor()).isEmpty();
    }
}
