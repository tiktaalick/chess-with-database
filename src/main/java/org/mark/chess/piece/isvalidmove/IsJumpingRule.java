package org.mark.chess.piece.isvalidmove;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.rulesengine.Rule;

public class IsJumpingRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean create() {
        return false;
    }

    @Override
    public boolean isApplicable(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return isJumping(getGrid(), getFrom(), getTo());
    }

    boolean isJumping(Grid grid, @NotNull Field from, @NotNull Field to) {
        var step = new Coordinates(Integer.signum(to.getCoordinates().getX() - from.getCoordinates().getX()),
                Integer.signum(to.getCoordinates().getY() - from.getCoordinates().getY()));

        var currentCoordinates = new Coordinates(from.getCoordinates().getX(), from.getCoordinates().getY());

        doNextStep(currentCoordinates, step);

        var mustIJump = false;

        while (!mustIJump && movingTowardsDestination(currentCoordinates, to.getCoordinates(), step)) {
            mustIJump = isFieldOccupied(grid, currentCoordinates);
            doNextStep(currentCoordinates, step);
        }

        return mustIJump;
    }

    private static void doNextStep(@NotNull Coordinates coordinates, @NotNull Coordinates step) {
        coordinates.setX(coordinates.getX() + step.getX());
        coordinates.setY(coordinates.getY() + step.getY());
    }

    private static boolean isFieldOccupied(@NotNull Grid grid, Coordinates currentCoordinates) {
        return grid
                .getFields()
                .stream()
                .filter(field -> field.getCoordinates().getX() == currentCoordinates.getX() &&
                        field.getCoordinates().getY() == currentCoordinates.getY())
                .anyMatch(field -> field.getPieceType() != null);
    }

    private static boolean movingTowardsDestination(@NotNull Coordinates currentCoordinates, @NotNull Coordinates to, Coordinates step) {
        return !(currentCoordinates.getX() == to.getX() && currentCoordinates.getY() == to.getY()) &&
                (step.getX() >= ZERO_STEPS) == (currentCoordinates.getX() <= to.getX()) &&
                (step.getY() >= ZERO_STEPS) == (currentCoordinates.getY() <= to.getY());
    }
}
