package org.mark.chess.logic;

import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Piece;

import java.util.List;
import java.util.stream.Collectors;

public interface PieceLogic {
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

    default int getAbsoluteHorizontalMove(Field from, Field to) {
        return Math.abs(to.getCoordinates().getX() - from.getCoordinates().getX());
    }

    default int getAbsoluteVerticalMove(Field from, Field to) {
        return Math.abs(to.getCoordinates().getY() - from.getCoordinates().getY());
    }

    default List<Field> getValidMoves(Grid grid, Field from) {
        return grid.getFields().stream().filter(to -> isValidMove(grid, from, to, false)).collect(Collectors.toList());
    }

    default boolean hasEmptyParameters(Grid grid, Field from, Field to) {
        return grid == null || from == null || to == null;
    }

    default boolean isFriendlyFire(Piece piece, Field to) {
        return to.getPiece() != null && to.getPiece().getColor() == piece.getColor();
    }

    default boolean isJumping(Grid grid, Field from, Field to) {
        Coordinates step = new Coordinates(Integer.signum(to.getCoordinates().getX() - from.getCoordinates().getX()),
                Integer.signum(to.getCoordinates().getY() - from.getCoordinates().getY()));

        Coordinates currentCoordinates = new Coordinates(from.getCoordinates().getX(), from.getCoordinates().getY());

        doNextStep(currentCoordinates, step);

        boolean mustIJump = false;

        while (!mustIJump && movingTowardsDestination(currentCoordinates, to.getCoordinates(), step)) {
            mustIJump = isFieldOccupied(grid, currentCoordinates);
            doNextStep(currentCoordinates, step);
        }

        return mustIJump;
    }

    boolean isValidMove(Grid grid, Field from, Field to, boolean isOpponent);
}
