package org.mark.chess.rulesengine.rule.isvalidmove;

import lombok.Data;
import org.mark.chess.enums.Color;
import org.mark.chess.logic.CheckLogic;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mark.chess.enums.PieceType.PAWN;

@Data
public class PieceTypeSharedRules {
    private static final int TWO_STEPS_FORWARD_FROM_BASELINE = 2;

    private IsValidMoveParameter isValidMoveParameter;
    private boolean              isValidMove;
    private int                  absoluteHorizontalMove;
    private int                  absoluteVerticalMove;

    protected static boolean isCaptureMove(Field from, Field to) {
        return to.getPiece() != null && to.getPiece().getColor() != from.getPiece().getColor();
    }

    protected static List<Field> neighbourFieldsWithOpponentPawns(Grid grid, Field playerField, Color color) {
        return grid
                .getFields()
                .stream()
                .filter(opponentField -> (opponentField.getCoordinates().getX() - 1 == playerField.getCoordinates().getX() ||
                        opponentField.getCoordinates().getX() + 1 == playerField.getCoordinates().getX()) &&
                        opponentField.getCoordinates().getY() == playerField.getCoordinates().getY())
                .filter(opponentField -> opponentField.getPiece() != null && opponentField.getPiece().getColor() != color)
                .filter(opponentField -> opponentField.getPiece().getPieceType() == PAWN)
                .collect(Collectors.toList());
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

    protected Field getFrom() {
        return Optional.ofNullable(isValidMoveParameter).map(IsValidMoveParameter::getFrom).orElse(null);
    }

    protected Grid getGrid() {
        return Optional.ofNullable(isValidMoveParameter).map(IsValidMoveParameter::getGrid).orElse(null);
    }

    protected Field getTo() {
        return Optional.ofNullable(isValidMoveParameter).map(IsValidMoveParameter::getTo).orElse(null);
    }

    protected boolean pawnIsValidBaselineMove() {
        return !isCaptureMove(getFrom(), getTo()) &&
                getFrom().getPiece().getColor().getBaselinePawn() == getFrom().getCoordinates().getY() &&
                getAbsoluteHorizontalMove(getFrom(), getTo()) == 0 &&
                getAbsoluteVerticalMove(getFrom(), getTo()) == TWO_STEPS_FORWARD_FROM_BASELINE;
    }

    protected void setParameter(IsValidMoveParameter isValidMoveParameter) {
        this.isValidMoveParameter = isValidMoveParameter;
        this.absoluteHorizontalMove = getAbsoluteHorizontalMove(getFrom(), getTo());
        this.absoluteVerticalMove = getAbsoluteVerticalMove(getFrom(), getTo());
    }

    CheckLogic getCheckLogic() {
        return isValidMoveParameter.getCheckLogic();
    }

    boolean isOpponent() {
        return Optional.ofNullable(isValidMoveParameter).map(IsValidMoveParameter::isOpponent).orElse(false);
    }
}
