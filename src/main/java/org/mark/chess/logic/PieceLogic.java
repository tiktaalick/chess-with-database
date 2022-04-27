package org.mark.chess.logic;

import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Piece;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface PieceLogic {
    BackgroundColorFactory backgroundColorFactory = new BackgroundColorFactory();

    default int getAbsoluteHorizontalMove(Field from, Field to) {
        return Math.abs(to.getCoordinates().getX() - from.getCoordinates().getX());
    }

    default int getAbsoluteVerticalMove(Field from, Field to) {
        return Math.abs(to.getCoordinates().getY() - from.getCoordinates().getY());
    }

    default List<Field> getValidMoves(List<Field> grid, Field from, PieceLogicFactory opponentFactory) {
        return grid.stream().filter(to -> isValidMove(grid, from, to, opponentFactory, false)).collect(Collectors.toList());
    }

    default boolean hasEmptyParameters(List<Field> grid, Field from, Field to, PieceLogicFactory opponentFactory) {
        return grid == null || from == null || to == null || opponentFactory == null;
    }

    default boolean isFriendlyFire(Piece piece, Field to) {
        return to.getPiece() != null && to.getPiece().getColor() == piece.getColor();
    }

    default boolean isJumping(List<Field> grid, Field from, Field to) {
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

    default boolean isMovingIntoCheck(List<Field> grid,
            Field from,
            Field to,
            boolean isOpponent,
            PieceLogicFactory opponentFactory,
            GridLogic gridLogic) {
        if (isOpponent) {
            return false;
        }

        List<Field> futureList = grid
                .stream()
                .filter(field -> !Arrays.asList(from.getCode(), to.getCode()).contains(field.getCode()))
                .collect(Collectors.toList());

        List<Field> movementList = grid
                .stream()
                .filter(field -> Arrays.asList(from.getCode(), to.getCode()).contains(field.getCode()))
                .map(field -> Objects.equals(field.getCode(), from.getCode())
                        ? new Field().setCoordinates(from.getCoordinates())
                        : new Field().setCoordinates(to.getCoordinates()).setPiece(from.getPiece()))
                .collect(Collectors.toList());

        futureList.addAll(movementList);

        Field kingField = gridLogic.getKingField(futureList, from.getPiece().getColor());

        List<Field> attackers = futureList
                .stream()
                .filter(opponentField -> opponentField.getPiece() != null)
                .filter(opponentField -> opponentField.getPiece().getColor() != from.getPiece().getColor())
                .filter(opponentField -> opponentFactory
                        .getLogic(opponentField.getPiece().getPieceType())
                        .isValidMove(futureList, opponentField, kingField, opponentFactory, true))
                .collect(Collectors.toList());

        return !attackers.isEmpty();
    }

    boolean isValidMove(List<Field> grid, Field from, Field to, PieceLogicFactory opponentFactory, boolean isOpponent);

    private void doNextStep(Coordinates coordinates, Coordinates step) {
        coordinates.setX(coordinates.getX() + step.getX());
        coordinates.setY(coordinates.getY() + step.getY());
    }

    private boolean isFieldOccupied(List<Field> grid, Coordinates currentCoordinates) {
        return grid
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
