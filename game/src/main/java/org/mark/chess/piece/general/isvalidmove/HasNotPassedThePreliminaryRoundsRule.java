package org.mark.chess.piece.general.isvalidmove;

import org.mark.chess.board.Coordinates;
import org.mark.chess.rulesengine.Rule;

public class HasNotPassedThePreliminaryRoundsRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean getResult() {
        return false;
    }

    @Override
    public boolean stopProcessingfurtherRulesAndGetResultNow(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return !getFrom()
                .getPieceType()
                .createCandidateToFieldCoordinates(getFrom())
                .stream()
                .map(Coordinates::createId)
                .toList()
                .contains(Coordinates.createId(getTo().getCoordinates()));
    }
}
