package org.mark.chess.logic;

import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Piece;

import java.util.List;

public interface PieceLogic {
    default boolean isFriendlyFire(Piece piece, Field to) {
        return to.piece() != null || to.piece().color() == piece.color();
    }

    default boolean isJumping(List<Field> grid, Field from, Field to) {
        final Coordinates step = new Coordinates(
                (int) Math.signum(to.coordinates().x() - from.coordinates().x()),
                (int) Math.signum(to.coordinates().y() - from.coordinates().y()));

        Coordinates currentCoordinates = new Coordinates(from.coordinates().x(), from.coordinates().y());

        doNextStep(currentCoordinates, step);

        boolean mustIJump = false;

        while (!mustIJump && movingTowardsDestination(currentCoordinates, to.coordinates(), step)) {
            mustIJump = isFieldOccupied(grid, currentCoordinates);
            doNextStep(currentCoordinates, step);
        }

        return mustIJump;
    }

    private boolean isFieldOccupied(List<Field> grid, Coordinates currentCoordinates) {
        return grid.stream()
                .filter(field -> field.coordinates().x() == currentCoordinates.x() && field.coordinates().y() == currentCoordinates.y())
                .anyMatch(field -> field.piece() != null);
    }

    private boolean movingTowardsDestination(Coordinates currentCoordinates, Coordinates to,
                                             Coordinates step) {
        return (currentCoordinates.x() * step.x()) < to.x() && (currentCoordinates.y() * step.y()) < to.y();
    }

    private void doNextStep(Coordinates coordinates, Coordinates step) {
        coordinates.x(coordinates.x() + step.x());
        coordinates.y(coordinates.y() + step.y());
    }

    boolean isMoveAllowed(List<Field> grid, Field from, Field to);
}
