package org.mark.chess.rulesengine.rule.piecetype;

import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;

public class IsJumpingRule extends PieceTypeHelper implements Rule<IsValidMoveParameter, Boolean> {
    @Override
    public Boolean create() {
        return this.isValidMove;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        this.isValidMove = !isJumping(isValidMoveParameter.getGrid(), isValidMoveParameter.getFrom(), isValidMoveParameter.getTo());
        return !this.isValidMove;
    }

    boolean isJumping(Grid grid, Field from, Field to) {
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

    private static void doNextStep(Coordinates coordinates, Coordinates step) {
        coordinates.setX(coordinates.getX() + step.getX());
        coordinates.setY(coordinates.getY() + step.getY());
    }

    private static boolean isFieldOccupied(Grid grid, Coordinates currentCoordinates) {
        return grid
                .getFields()
                .stream()
                .filter(field -> field.getCoordinates().getX() == currentCoordinates.getX() &&
                        field.getCoordinates().getY() == currentCoordinates.getY())
                .anyMatch(field -> field.getPiece() != null);
    }

    private static boolean movingTowardsDestination(Coordinates currentCoordinates, Coordinates to, Coordinates step) {
        return !(currentCoordinates.getX() == to.getX() && currentCoordinates.getY() == to.getY()) &&
                (step.getX() >= 0) == (currentCoordinates.getX() <= to.getX()) &&
                (step.getY() >= 0) == (currentCoordinates.getY() <= to.getY());
    }
}
