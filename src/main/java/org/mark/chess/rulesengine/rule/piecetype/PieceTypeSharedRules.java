package org.mark.chess.rulesengine.rule.piecetype;

import lombok.Data;
import org.mark.chess.logic.CheckLogic;
import org.mark.chess.logic.GridLogic;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;

import java.util.Optional;

@Data
public class PieceTypeSharedRules {
    private IsValidMoveParameter isValidMoveParameter;
    private boolean              isValidMove;
    private int                  absoluteHorizontalMove;
    private int                  absoluteVerticalMove;

    protected static boolean isCaptureMove(Field from, Field to) {
        return to.getPiece() != null && to.getPiece().getColor() != from.getPiece().getColor();
    }

    protected int getAbsoluteHorizontalMove(Field from, Field to) {
        return from == null || to == null
                ? 0
                : Math.abs(to.getCoordinates().getX() - from.getCoordinates().getX());
    }

    protected int getAbsoluteVerticalMove(Field from, Field to) {
        return from == null || to == null
                ? 0
                : Math.abs(to.getCoordinates().getY() - from.getCoordinates().getY());
    }

    protected CheckLogic getCheckLogic() {
        return isValidMoveParameter.getCheckLogic();
    }

    protected Field getFrom() {
        return Optional.ofNullable(isValidMoveParameter).map(IsValidMoveParameter::getFrom).orElse(null);
    }

    protected Grid getGrid() {
        return Optional.ofNullable(isValidMoveParameter).map(IsValidMoveParameter::getGrid).orElse(null);
    }

    protected GridLogic getGridLogic() {
        return Optional.ofNullable(isValidMoveParameter).map(IsValidMoveParameter::getGridLogic).orElse(null);
    }

    protected Field getTo() {
        return Optional.ofNullable(isValidMoveParameter).map(IsValidMoveParameter::getTo).orElse(null);
    }

    protected boolean isOpponent() {
        return Optional.ofNullable(isValidMoveParameter).map(IsValidMoveParameter::isOpponent).orElse(false);
    }

    protected void setParameter(IsValidMoveParameter isValidMoveParameter) {
        this.isValidMoveParameter = isValidMoveParameter;
        this.absoluteHorizontalMove = getAbsoluteHorizontalMove(getFrom(), getTo());
        this.absoluteVerticalMove = getAbsoluteVerticalMove(getFrom(), getTo());
    }
}
