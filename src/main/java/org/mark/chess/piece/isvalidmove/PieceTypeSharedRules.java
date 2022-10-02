package org.mark.chess.piece.isvalidmove;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.player.PlayerColor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mark.chess.piece.PieceType.PAWN;

@Data
public class PieceTypeSharedRules {

    protected static final int ONE_STEP   = 1;
    protected static final int TWO_STEPS  = 2;
    protected static final int ZERO_STEPS = 0;

    private static final int ONE_STEP_EAST                   = 1;
    private static final int ONE_STEP_WEST                   = -1;
    private static final int TWO_STEPS_FORWARD_FROM_BASELINE = 2;

    private IsValidMoveParameter isValidMoveParameter;
    private boolean              isValidMove;
    private int                  absoluteHorizontalMove;
    private int                  absoluteVerticalMove;

    protected static boolean isCaptureMove(Field from, @NotNull Field to) {
        return to.getPieceType() != null && to.getPieceType().getColor() != from.getPieceType().getColor();
    }

    protected static List<Field> neighbourFieldsWithOpponentPawns(@NotNull Grid grid, Field playerField, PlayerColor color) {
        return grid
                .getFields()
                .stream()
                .filter(opponentField -> (opponentField.getCoordinates().getX() + ONE_STEP_WEST == playerField.getCoordinates().getX() ||
                        opponentField.getCoordinates().getX() + ONE_STEP_EAST == playerField.getCoordinates().getX()) &&
                        opponentField.getCoordinates().getY() == playerField.getCoordinates().getY())
                .filter(opponentField -> opponentField.getPieceType() != null && opponentField.getPieceType().getColor() != color)
                .filter(opponentField -> opponentField.getPieceType().getName().equals(PAWN))
                .collect(Collectors.toList());
    }

    protected int getAbsoluteHorizontalMove(Field from, Field to) {
        return from == null || to == null
                ? ZERO_STEPS
                : Math.abs(to.getCoordinates().getX() - from.getCoordinates().getX());
    }

    protected int getAbsoluteVerticalMove(Field from, Field to) {
        return from == null || to == null
                ? ZERO_STEPS
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

    protected boolean isDiagonalMove() {
        return getAbsoluteHorizontalMove() != ZERO_STEPS && getAbsoluteHorizontalMove() == getAbsoluteVerticalMove();
    }

    protected boolean isHorizontalMove() {
        return getAbsoluteHorizontalMove() != ZERO_STEPS && getAbsoluteVerticalMove() == ZERO_STEPS;
    }

    protected boolean isOpponent() {
        return Optional.ofNullable(isValidMoveParameter).map(IsValidMoveParameter::isOpponent).orElse(false);
    }

    protected boolean isVerticalMove() {
        return getAbsoluteHorizontalMove() == ZERO_STEPS && getAbsoluteVerticalMove() != ZERO_STEPS;
    }

    protected boolean pawnIsValidBaselineMove() {
        return !isCaptureMove(getFrom(), getTo()) &&
                getFrom().getPieceType().getColor().getBaselinePawn() == getFrom().getCoordinates().getY() &&
                getAbsoluteHorizontalMove(getFrom(), getTo()) == 0 &&
                getAbsoluteVerticalMove(getFrom(), getTo()) == TWO_STEPS_FORWARD_FROM_BASELINE;
    }

    protected void setParameter(IsValidMoveParameter isValidMoveParameter) {
        this.isValidMoveParameter = isValidMoveParameter;
        this.absoluteHorizontalMove = getAbsoluteHorizontalMove(getFrom(), getTo());
        this.absoluteVerticalMove = getAbsoluteVerticalMove(getFrom(), getTo());
    }
}
