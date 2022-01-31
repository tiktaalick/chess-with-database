package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Field;
import org.mark.chess.model.Pawn;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PawnLogic implements PieceLogic {
    @Autowired
    private FieldLogic fieldLogic;

    @Override
    public boolean isValidMove(List<Field> grid,
                               Field from,
                               Field to,
                               PieceLogicFactory opponentFactory,
                               boolean isOpponent) {
        return !this.isFriendlyFire(from.piece(), to) &&
                !isJumping(grid, from, to) &&
                !isInCheck(grid, from, to, isOpponent, opponentFactory, fieldLogic) &&
                isValidDirection(from, to) &&
                (isValidBasicMove(from, to) || isValidBaselineMove(from, to) ||
                        isValidCaptureMove(from, to) || isValidEnPassantMove(grid, from, to));
    }

    private boolean isValidDirection(Field from, Field to) {
        return Integer.signum(to.coordinates().y() - from.coordinates().y()) ==
                (from.piece().color() == Color.BLACK ? 1 : -1);
    }

    private boolean isValidBasicMove(Field from, Field to) {
        return !isCaptureMove(from, to) &&
                getAbsoluteHorizontalMove(from, to) == 0 && getAbsoluteVerticalMove(from, to) == 1;
    }

    private boolean isValidBaselineMove(Field from, Field to) {
        return !isCaptureMove(from, to) && Arrays.asList(1, 6).contains(from.coordinates().y()) &&
                getAbsoluteHorizontalMove(from, to) == 0 && getAbsoluteVerticalMove(from, to) == 2;
    }

    private boolean isValidCaptureMove(Field from, Field to) {
        return isCaptureMove(from, to) &&
                getAbsoluteHorizontalMove(from, to) == 1 && getAbsoluteVerticalMove(from, to) == 1;
    }

    private boolean isValidEnPassantMove(List<Field> grid, Field from, Field to) {
        return neighbourFieldsWithOpponentPawns(grid, from, from.piece().color()).stream()
                .filter(field -> ((Pawn) field.piece()).mayBeCapturedEnPassant())
                .filter(field -> field.coordinates().x() == to.coordinates().x())
                .anyMatch(field -> getAbsoluteVerticalMove(field, to) == 1);
    }

    private boolean isCaptureMove(Field from, Field to) {
        return to.piece() != null && to.piece().color() != from.piece().color();
    }

    public boolean mayBeCapturedEnPassant(List<Field> grid, Field from, Field to) {
        return isValidBaselineMove(from, to) && !neighbourFieldsWithOpponentPawns(grid, to, from.piece().color()).isEmpty();
    }

    private List<Field> neighbourFieldsWithOpponentPawns(List<Field> grid, Field playerField, Color color) {
        return grid.stream()
                .filter(opponentField -> (opponentField.coordinates().x() - 1 == playerField.coordinates().x() ||
                        opponentField.coordinates().x() + 1 == playerField.coordinates().x()) &&
                        opponentField.coordinates().y() == playerField.coordinates().y())
                .filter(opponentField -> opponentField.piece() != null && opponentField.piece().color() != color)
                .filter(opponentField -> opponentField.piece().pieceType() == PieceType.PAWN)
                .collect(Collectors.toList());
    }
}
