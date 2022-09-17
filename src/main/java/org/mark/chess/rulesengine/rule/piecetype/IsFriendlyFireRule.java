package org.mark.chess.rulesengine.rule.piecetype;

import org.mark.chess.model.Field;
import org.mark.chess.model.Piece;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class IsFriendlyFireRule extends PieceTypeHelper implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return this.isValidMove;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        this.isValidMove = !isFriendlyFire(isValidMoveParameter.getFrom().getPiece(), isValidMoveParameter.getTo());
        return !this.isValidMove;
    }

    boolean isFriendlyFire(Piece piece, Field to) {
        return to.getPiece() != null && to.getPiece().getColor() == piece.getColor();
    }
}
