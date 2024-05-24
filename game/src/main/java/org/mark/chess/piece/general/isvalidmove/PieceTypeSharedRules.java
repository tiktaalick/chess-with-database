package org.mark.chess.piece.general.isvalidmove;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.player.PlayerColor;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static org.mark.chess.board.Chessboard.NUMBER_OF_COLUMNS_AND_ROWS;
import static org.mark.chess.piece.general.PieceType.PAWN;

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

    public static @NotNull Stream<Coordinates> diagonalMoves(Field from, int number) {
        return Stream.of(new Coordinates(number, from.getCoordinates().getY() + from.getCoordinates().getX() - number),
                new Coordinates(number, from.getCoordinates().getY() - from.getCoordinates().getX() + number));
    }

    public static @NotNull Stream<Coordinates> horizontalAndVerticalMoves(Field from, int number) {
        return Stream.of(new Coordinates(number, from.getCoordinates().getY()), new Coordinates(from.getCoordinates().getX(), number));
    }

    public static @NotNull Predicate<Coordinates> maxStepsHorizontally(Field from, int maxStep) {
        return coordinates -> abs(coordinates.getX() - from.getCoordinates().getX()) <= maxStep;
    }

    public static @NotNull Predicate<Coordinates> maxStepsVertically(Field from, int maxStep) {
        return coordinates -> abs(coordinates.getY() - from.getCoordinates().getY()) <= maxStep;
    }

    public static @NotNull Predicate<Coordinates> minStepsVertically(Field from) {
        return coordinates -> abs(coordinates.getY() - from.getCoordinates().getY()) >= 1;
    }

    public static @NotNull Predicate<Coordinates> skipFrom(Field from) {
        return coordinates -> !(coordinates.getX() == from.getCoordinates().getX() && coordinates.getY() == from.getCoordinates().getY());
    }

    public static @NotNull Predicate<Coordinates> withinChessboardBoundaries() {
        return coordinates -> coordinates.getY() >= 1 && coordinates.getY() <= NUMBER_OF_COLUMNS_AND_ROWS;
    }

    public String getContext() {
        return "move from " +
                getFrom() +
                " to " +
                getTo() +
                " on chessboard " +
                Optional.ofNullable(isValidMoveParameter).map(IsValidMoveParameter::getChessboard).orElse(null);
    }

    public Level getLogLevel() {
        return Level.OFF;
    }

    protected static boolean isCaptureMove(Field from, @NotNull Field to) {
        return to.getPieceType() != null && to.getPieceType().getColor() != from.getPieceType().getColor();
    }

    protected static List<Field> neighbourFieldsWithOpponentPawns(@NotNull Chessboard chessboard, Field playerField, PlayerColor color) {
        return chessboard
                .getFields()
                .stream()
                .filter(opponentField -> (opponentField.getCoordinates().getX() + ONE_STEP_WEST == playerField.getCoordinates().getX() ||
                        opponentField.getCoordinates().getX() + ONE_STEP_EAST == playerField.getCoordinates().getX()) &&
                        opponentField.getCoordinates().getY() == playerField.getCoordinates().getY())
                .filter(opponentField -> opponentField.getPieceType() != null && opponentField.getPieceType().getColor() != color)
                .filter(opponentField -> opponentField.getPieceType().getName().equals(PAWN))
                .toList();
    }

    protected int getAbsoluteHorizontalMove(Field from, Field to) {
        return from == null || to == null ? ZERO_STEPS : Math.abs(to.getCoordinates().getX() - from.getCoordinates().getX());
    }

    protected int getAbsoluteVerticalMove(Field from, Field to) {
        return from == null || to == null ? ZERO_STEPS : Math.abs(to.getCoordinates().getY() - from.getCoordinates().getY());
    }

    protected Chessboard getChessboard() {
        return Optional.ofNullable(isValidMoveParameter).map(IsValidMoveParameter::getChessboard).orElse(null);
    }

    protected Field getFrom() {
        return Optional.ofNullable(isValidMoveParameter).map(IsValidMoveParameter::getFrom).orElse(null);
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
