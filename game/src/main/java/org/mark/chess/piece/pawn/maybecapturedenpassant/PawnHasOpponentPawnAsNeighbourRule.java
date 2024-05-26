package org.mark.chess.piece.pawn.maybecapturedenpassant;

import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules;
import org.mark.chess.rulesengine.Rule;

public class PawnHasOpponentPawnAsNeighbourRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

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

        return !neighbourFieldsWithOpponentPawns(this.isValidMoveParameter.getChessboard(),
                this.isValidMoveParameter.getTo(),
                this.isValidMoveParameter.getFrom().getPieceType().getColor()).isEmpty();
    }
}
