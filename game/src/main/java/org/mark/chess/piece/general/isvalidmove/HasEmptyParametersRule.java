package org.mark.chess.piece.general.isvalidmove;

import org.mark.chess.rulesengine.Rule;

public class HasEmptyParametersRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean getResult() {
        return false;
    }

    @Override
    public boolean stopProcessingfurtherRulesAndGetResultNow(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return isValidMoveParameter == null || getChessboard() == null || getFrom() == null || getTo() == null;
    }
}
