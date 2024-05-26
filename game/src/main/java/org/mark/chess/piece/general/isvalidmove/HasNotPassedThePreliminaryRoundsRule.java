package org.mark.chess.piece.general.isvalidmove;

import org.mark.chess.board.Coordinates;
import org.mark.chess.rulesengine.Rule;

public class HasNotPassedThePreliminaryRoundsRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

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

        return !this.isValidMoveParameter
                .getFrom()
                .getPieceType()
                .createCandidateToFieldCoordinates(this.isValidMoveParameter.getFrom())
                .stream()
                .map(Coordinates::createId)
                .toList()
                .contains(Coordinates.createId(this.isValidMoveParameter.getTo().getCoordinates()));
    }
}
