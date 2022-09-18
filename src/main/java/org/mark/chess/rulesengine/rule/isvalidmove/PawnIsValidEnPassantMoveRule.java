package org.mark.chess.rulesengine.rule.isvalidmove;

import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Pawn;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class PawnIsValidEnPassantMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {
    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return isValidEnPassantMove(getGrid(), getFrom(), getTo());
    }

    private boolean isValidEnPassantMove(Grid grid, Field from, Field to) {
        return neighbourFieldsWithOpponentPawns(grid, from, from.getPiece().getColor())
                .stream()
                .filter(field -> ((Pawn) field.getPiece()).isMayBeCapturedEnPassant())
                .filter(field -> field.getCoordinates().getX() == to.getCoordinates().getX())
                .anyMatch(field -> getAbsoluteVerticalMove(field, to) == 1);
    }
}
