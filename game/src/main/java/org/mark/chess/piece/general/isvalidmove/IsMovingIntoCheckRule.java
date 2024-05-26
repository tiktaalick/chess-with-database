package org.mark.chess.piece.general.isvalidmove;

import org.mark.chess.rulesengine.Rule;

public class IsMovingIntoCheckRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

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

        return this.isValidMoveParameter
                .getFrom()
                .isMovingIntoCheck(this.isValidMoveParameter.getChessboard(),
                        this.isValidMoveParameter.getTo(),
                        this.isValidMoveParameter.isOpponent());
    }
}
