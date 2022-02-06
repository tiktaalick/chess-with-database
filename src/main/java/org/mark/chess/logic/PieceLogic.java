package org.mark.chess.logic;

import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Piece;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface PieceLogic {
    default List<Field> getValidMoves(List<Field> grid, Field from, PieceLogicFactory opponentFactory) {
        return grid.stream().filter(to -> isCancelMove(from, to) ||
                isValidMove(grid, from, to, opponentFactory, false)).collect(Collectors.toList());
    }

    default boolean isCancelMove(Field from, Field to) {
        return from.coordinates().x() == to.coordinates().x() &&
                from.coordinates().y() == to.coordinates().y();
    }

    default boolean isFriendlyFire(Piece piece, Field to) {
        return to.piece() != null && to.piece().color() == piece.color();
    }

    default boolean isJumping(List<Field> grid, Field from, Field to) {
        final Coordinates step = new Coordinates(
                Integer.signum(to.coordinates().x() - from.coordinates().x()),
                Integer.signum(to.coordinates().y() - from.coordinates().y()));

        Coordinates currentCoordinates = new Coordinates(from.coordinates().x(), from.coordinates().y());

        doNextStep(currentCoordinates, step);

        boolean mustIJump = false;

        while (!mustIJump && movingTowardsDestination(currentCoordinates, to.coordinates(), step)) {
            mustIJump = isFieldOccupied(grid, currentCoordinates);
            doNextStep(currentCoordinates, step);
        }

        return mustIJump;
    }

    private void doNextStep(Coordinates coordinates, Coordinates step) {
        coordinates.x(coordinates.x() + step.x());
        coordinates.y(coordinates.y() + step.y());
    }

    default int getAbsoluteHorizontalMove(Field from, Field to) {
        return Math.abs(to.coordinates().x() - from.coordinates().x());
    }

    default int getAbsoluteVerticalMove(Field from, Field to) {
        return Math.abs(to.coordinates().y() - from.coordinates().y());
    }

    default boolean isInCheck(List<Field> grid, Field from, Field to, boolean isOpponent,
                              PieceLogicFactory opponentFactory, GridLogic gridLogic) {
        if (isOpponent) {
            return false;
        }
        Field kingField = gridLogic.getKingField(grid, from.piece().color());

        List<Field> futureList = grid.stream()
                .filter(field -> !Arrays.asList(from.id(), to.id()).contains(field.id()))
                .collect(Collectors.toList());

        List<Field> movementList = grid.stream()
                .filter(field -> Arrays.asList(from.id(), to.id()).contains(field.id()))
                .map(field -> field.id() == from.id()
                        ? new Field().coordinates(from.coordinates())
                        : new Field().coordinates(to.coordinates()).piece(from.piece()))
                .collect(Collectors.toList());

        futureList.addAll(movementList);

        return grid.stream()
                .filter(opponentField -> opponentField.piece() != null)
                .filter(opponentField -> opponentField.piece().color() != from.piece().color())
                .anyMatch(opponentField -> opponentFactory.getLogic(opponentField.piece().pieceType())
                        .isValidMove(futureList, opponentField, kingField, opponentFactory, true));
    }

    private boolean isFieldOccupied(List<Field> grid, Coordinates currentCoordinates) {
        return grid.stream()
                .filter(field -> field.coordinates().x() == currentCoordinates.x() && field.coordinates().y() == currentCoordinates.y())
                .anyMatch(field -> field.piece() != null);
    }

    private boolean movingTowardsDestination(Coordinates currentCoordinates, Coordinates to, Coordinates step) {
        return !(currentCoordinates.x() == to.x() && currentCoordinates.y() == to.y()) &&
                (step.x() >= 0) == (currentCoordinates.x() <= to.x()) &&
                (step.y() >= 0) == (currentCoordinates.y() <= to.y());
    }

    boolean isValidMove(List<Field> grid,
                        Field from,
                        Field to,
                        PieceLogicFactory opponentFactory,
                        boolean isOpponent);
}
