package org.mark.chess.logic;

import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Piece;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public abstract class PieceLogic {
    final CheckLogic checkLogic;
    final GridLogic  gridLogic;

    protected PieceLogic(CheckLogic checkLogic, GridLogic gridLogic) {
        this.checkLogic = checkLogic;
        this.gridLogic = gridLogic;
    }

    protected List<Field> getValidMoves(Grid grid, Field from) {
        return grid.getFields().stream().filter(to -> isValidMove(grid, from, to, false)).collect(Collectors.toList());
    }

    int getAbsoluteHorizontalMove(Field from, Field to) {
        return Math.abs(to.getCoordinates().getX() - from.getCoordinates().getX());
    }

    int getAbsoluteVerticalMove(Field from, Field to) {
        return Math.abs(to.getCoordinates().getY() - from.getCoordinates().getY());
    }

    boolean hasEmptyParameters(Grid grid, Field from, Field to) {
        return grid == null || from == null || to == null;
    }

    boolean isFriendlyFire(Piece piece, Field to) {
        return to.getPiece() != null && to.getPiece().getColor() == piece.getColor();
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

    abstract boolean isValidMove(Grid grid, Field from, Field to, boolean isOpponent);

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
