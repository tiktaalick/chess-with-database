package org.mark.chess.logic;

import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Piece;

import java.util.List;
import java.util.stream.Collectors;

public interface PieceLogic {
    BackgroundColorFactory backgroundColorFactory = new BackgroundColorFactory();

    default int getAbsoluteHorizontalMove(Field from, Field to) {
        return Math.abs(to.getCoordinates().getX() - from.getCoordinates().getX());
    }

    default int getAbsoluteVerticalMove(Field from, Field to) {
        return Math.abs(to.getCoordinates().getY() - from.getCoordinates().getY());
    }

    default List<Field> getValidMoves(Grid grid, Field from, PieceLogicFactory opponentFactory) {
        return grid.getFields().stream().filter(to -> isValidMove(grid, from, to, opponentFactory, false)).collect(Collectors.toList());
    }

    default boolean hasEmptyParameters(Grid grid, Field from, Field to, PieceLogicFactory opponentFactory) {
        return grid == null || from == null || to == null || opponentFactory == null;
    }

    default boolean isFriendlyFire(Piece piece, Field to) {
        return to.getPiece() != null && to.getPiece().getColor() == piece.getColor();
    }

    default boolean isJumping(Grid grid, Field from, Field to) {
        final Coordinates step = new Coordinates(Integer.signum(to.getCoordinates().getX() - from.getCoordinates().getX()),
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

    default boolean isMovingIntoCheck(Grid grid, Field from, Field to, boolean isOpponent, PieceLogicFactory opponentFactory) {
        if (isOpponent) {
            return false;
        }

        Grid gridAfterMovement = new Grid(grid, from, to);

        List<Field> attackers = gridAfterMovement
                .getFields()
                .stream()
                .filter(opponentField -> opponentField.getPiece() != null)
                .filter(opponentField -> opponentField.getPiece().getColor() != from.getPiece().getColor())
                .filter(opponentField -> opponentFactory
                        .getLogic(opponentField.getPiece().getPieceType())
                        .isValidMove(gridAfterMovement, opponentField, gridAfterMovement.getKingField(), opponentFactory, true))
                .collect(Collectors.toList());

        return !attackers.isEmpty();
    }

    boolean isValidMove(Grid grid, Field from, Field to, PieceLogicFactory opponentFactory, boolean isOpponent);

    private void doNextStep(Coordinates coordinates, Coordinates step) {
        coordinates.setX(coordinates.getX() + step.getX());
        coordinates.setY(coordinates.getY() + step.getY());
    }

    private boolean isFieldOccupied(Grid grid, Coordinates currentCoordinates) {
        return grid
                .getFields()
                .stream()
                .filter(field -> field.getCoordinates().getX() == currentCoordinates.getX() &&
                                 field.getCoordinates().getY() == currentCoordinates.getY())
                .anyMatch(field -> field.getPiece() != null);
    }

    private boolean movingTowardsDestination(Coordinates currentCoordinates, Coordinates to, Coordinates step) {
        return !(currentCoordinates.getX() == to.getX() && currentCoordinates.getY() == to.getY()) &&
               (step.getX() >= 0) == (currentCoordinates.getX() <= to.getX()) &&
               (step.getY() >= 0) == (currentCoordinates.getY() <= to.getY());
    }
}
