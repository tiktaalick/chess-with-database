package org.mark.chess.piece.general.isvalidmove;

import org.mark.chess.rulesengine.Rule;

public class IsFriendlyFireRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    private IsValidMoveParameter isValidMoveParameter;

    @Override
    public String getContext() {
        return super.getContext(isValidMoveParameter);
    }

    @Override
    public Boolean getResult() {
        return false;
    }

    @Override
    public boolean stopProcessingfurtherRulesAndGetResultNow(IsValidMoveParameter isValidMoveParameter) {
        this.isValidMoveParameter = isValidMoveParameter;

        return this.isValidMoveParameter.getFrom().getPieceType() != null &&
                this.isValidMoveParameter.getTo().getPieceType() != null &&
                this.isValidMoveParameter.getFrom().getPieceType().getColor() == this.isValidMoveParameter.getTo().getPieceType().getColor();
    }
}
