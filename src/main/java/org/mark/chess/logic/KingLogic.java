package org.mark.chess.logic;

import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.King;
import org.mark.chess.model.Piece;
import org.mark.chess.model.Rook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class KingLogic implements PieceLogic {
    public static final  int KING_X_LEFT       = 3;
    public static final  int KING_X_RIGHT      = 7;
    public static final  int ROOK_X_LEFT_FROM  = 1;
    public static final  int ROOK_X_LEFT_TO    = 4;
    public static final  int ROOK_X_RIGHT_FROM = 8;
    public static final  int ROOK_X_RIGHT_TO   = 6;
    private static final int KING_INITIAL_X    = 5;

    @Autowired
    private GridLogic gridLogic;

    @Override
    public boolean isValidMove(List<Field> grid, Field from, Field to, PieceLogicFactory opponentFactory, boolean isOpponent) {
        return !hasEmptyParameters(grid, from, to, opponentFactory) &&
               (isValidBasicMove(from, to) ||
                isValidCastling(grid, from, to, KING_X_LEFT, opponentFactory, isOpponent, false) ||
                isValidCastling(grid, from, to, KING_X_RIGHT, opponentFactory, isOpponent, false)) &&
               !this.isFriendlyFire(from.getPiece(), to) &&
               !isJumping(grid, from, to) &&
               !isMovingIntoCheck(grid, from, to, isOpponent, opponentFactory, gridLogic);
    }

    private boolean isValidBasicMove(Field from, Field to) {
        int horizontalMove = getAbsoluteHorizontalMove(from, to);
        int verticalMove = getAbsoluteVerticalMove(from, to);
        return Arrays.asList(0, 1).contains(horizontalMove) &&
               Arrays.asList(0, 1).contains(verticalMove) &&
               !(horizontalMove == 0 && verticalMove == 0);
    }

    public boolean isValidCastling(List<Field> grid,
            Field from,
            Field to,
            int direction,
            PieceLogicFactory opponentFactory,
            boolean isOpponent,
            boolean isNowCastling) {
        if (isOpponent) {
            return false;
        }

        Field rookField = gridLogic.getField(grid,
                new Coordinates((direction == KING_X_LEFT
                        ? ROOK_X_LEFT_FROM
                        : ROOK_X_RIGHT_FROM), from.getPiece().getColor().getBaseline()));

        boolean isValidFrom = from.getCoordinates().getX() == KING_INITIAL_X &&
                              from.getCoordinates().getY() == from.getPiece().getColor().getBaseline();
        boolean isValidTo = Arrays.asList(KING_X_LEFT, KING_X_RIGHT).contains(direction) &&
                            to.getCoordinates().getX() == direction &&
                            to.getCoordinates().getY() == from.getPiece().getColor().getBaseline();
        boolean isKingValid = isNowCastling || !((King) from.getPiece()).isHasMovedAtLeastOnce();
        boolean isRookValid = Optional.ofNullable(rookField).map(Field::getPiece).map(Piece::getPieceType).orElse(null) == PieceType.ROOK &&
                              !((Rook) rookField.getPiece()).isHasMovedAtLeastOnce();
        boolean isInCheck = isInCheckNow(grid, from, from, opponentFactory, false);

        return isValidFrom && isValidTo && isKingValid && isRookValid && !isInCheck;
    }

    private boolean isInCheckNow(List<Field> grid, Field from, Field to, PieceLogicFactory opponentFactory, boolean isOpponent) {
        if (isOpponent) {
            return false;
        }

        return grid
                .stream()
                .filter(opponentField -> opponentField.getPiece() != null)
                .filter(opponentField -> opponentField.getPiece().getColor() != from.getPiece().getColor())
                .anyMatch(opponentField -> opponentFactory
                        .getLogic(opponentField.getPiece().getPieceType())
                        .isValidMove(grid, opponentField, to, opponentFactory, true));
    }
}
