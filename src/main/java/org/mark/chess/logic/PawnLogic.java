package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Pawn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PawnLogic implements PieceLogic {
    private GridLogic  gridLogic;
    private CheckLogic checkLogic;

    @Autowired
    @Lazy
    public PawnLogic(GridLogic gridLogic, CheckLogic checkLogic) {
        this.gridLogic = gridLogic;
        this.checkLogic = checkLogic;
    }

    private static boolean isCaptureMove(Field from, Field to) {
        return to.getPiece() != null && to.getPiece().getColor() != from.getPiece().getColor();
    }

    private static boolean isValidDirection(Field from, Field to) {
        return Integer.signum(to.getCoordinates().getY() - from.getCoordinates().getY()) ==
               (from.getPiece().getColor() == Color.WHITE
                       ? 1
                       : -1);
    }

    private static List<Field> neighbourFieldsWithOpponentPawns(Grid grid, Field playerField, Color color) {
        return grid
                .getFields()
                .stream()
                .filter(opponentField -> (opponentField.getCoordinates().getX() - 1 == playerField.getCoordinates().getX() ||
                                          opponentField.getCoordinates().getX() + 1 == playerField.getCoordinates().getX()) &&
                                         opponentField.getCoordinates().getY() == playerField.getCoordinates().getY())
                .filter(opponentField -> opponentField.getPiece() != null && opponentField.getPiece().getColor() != color)
                .filter(opponentField -> opponentField.getPiece().getPieceType() == PieceType.PAWN)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValidMove(Grid grid, Field from, Field to, boolean isOpponent) {
        return !hasEmptyParameters(grid, from, to) &&
               (isValidBasicMove(from, to) ||
                isValidBaselineMove(from, to) ||
                isValidCaptureMove(from, to) ||
                isValidEnPassantMove(grid, from, to)) &&
               isValidDirection(from, to) &&
               !this.isFriendlyFire(from.getPiece(), to) &&
               !isJumping(grid, from, to) &&
               !checkLogic.isMovingIntoCheck(grid, from, to, isOpponent, gridLogic);
    }

    boolean isPawnBeingPromoted(Field from, Field to) {
        return from.getPiece().isPawnBeingPromoted() ||
               from.getCoordinates().getY() == from.getPiece().getColor().getOpposite().getBaseline() ||
               to.getCoordinates().getY() == from.getPiece().getColor().getOpposite().getBaseline();
    }

    boolean mayBeCapturedEnPassant(Grid grid, Field from, Field to) {
        return isValidBaselineMove(from, to) && !neighbourFieldsWithOpponentPawns(grid, to, from.getPiece().getColor()).isEmpty();
    }

    private boolean isValidBaselineMove(Field from, Field to) {
        return !isCaptureMove(from, to) &&
               from.getPiece().getColor().getBaselinePawn() == from.getCoordinates().getY() &&
               getAbsoluteHorizontalMove(from, to) == 0 &&
               getAbsoluteVerticalMove(from, to) == 2;
    }

    private boolean isValidBasicMove(Field from, Field to) {
        return !isCaptureMove(from, to) && getAbsoluteHorizontalMove(from, to) == 0 && getAbsoluteVerticalMove(from, to) == 1;
    }

    private boolean isValidCaptureMove(Field from, Field to) {
        return isCaptureMove(from, to) && getAbsoluteHorizontalMove(from, to) == 1 && getAbsoluteVerticalMove(from, to) == 1;
    }

    private boolean isValidEnPassantMove(Grid grid, Field from, Field to) {
        return neighbourFieldsWithOpponentPawns(grid, from, from.getPiece().getColor())
                .stream()
                .filter(field -> ((Pawn) field.getPiece()).isMayBeCapturedEnPassant())
                .filter(field -> field.getCoordinates().getX() == to.getCoordinates().getX())
                .anyMatch(field -> getAbsoluteVerticalMove(field, to) == 1);
    }
}
