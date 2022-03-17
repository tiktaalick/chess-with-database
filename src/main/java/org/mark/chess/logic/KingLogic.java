package org.mark.chess.logic;

import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class KingLogic implements PieceLogic {
    public static final int LEFT = 2;
    public static final int RIGHT = 6;
    public static final int ROOK_X_LEFT_FROM = 0;
    public static final int ROOK_X_LEFT_TO = 3;
    public static final int ROOK_X_RIGHT_FROM = 7;
    public static final int ROOK_X_RIGHT_TO = 5;
    private static final int KING_INITIAL_X = 4;

    @Autowired
    private GridLogic gridLogic;

    @Override
    public boolean isValidMove(List<Field> grid, Field from, Field to, PieceLogicFactory opponentFactory,
                               boolean isOpponent) {
        return !hasEmptyParameters(grid, from, to, opponentFactory) &&
               (isValidBasicMove(from, to) ||
                isValidCastling(grid, from, to, LEFT, opponentFactory, isOpponent, false) ||
                isValidCastling(grid, from, to, RIGHT, opponentFactory, isOpponent, false)) &&
               !this.isFriendlyFire(from.piece(), to) &&
               !isJumping(grid, from, to) &&
               !isInCheck(grid, from, to, opponentFactory, isOpponent);
    }

    private boolean isValidBasicMove(Field from, Field to) {
        int horizontalMove = getAbsoluteHorizontalMove(from, to);
        int verticalMove = getAbsoluteVerticalMove(from, to);
        return Arrays.asList(0, 1).contains(horizontalMove) &&
               Arrays.asList(0, 1).contains(verticalMove) &&
               !(horizontalMove == 0 && verticalMove == 0);
    }

    public boolean isValidCastling(List<Field> grid, Field from, Field to, int direction,
                                   PieceLogicFactory opponentFactory, boolean isOpponent, boolean isNowCastling) {
        if (isOpponent) {
            return false;
        }

        Field rookField = gridLogic.getField(grid,
                                             new Coordinates((direction == LEFT
                                                              ? ROOK_X_LEFT_FROM
                                                              : ROOK_X_RIGHT_FROM), from.piece().color().getBaselineY())
                                            );

        boolean isValidFrom = from.coordinates().x() == KING_INITIAL_X &&
                              from.coordinates().y() == from.piece().color().getBaselineY();
        boolean isValidTo = Arrays.asList(LEFT, RIGHT).contains(direction) &&
                            to.coordinates().x() == direction &&
                            to.coordinates().y() == from.piece().color().getBaselineY();
        boolean isKingValid = isNowCastling || !((King) from.piece()).hasMovedAtLeastOnce();
        boolean isRookValid = Optional.ofNullable(rookField).map(Field::piece).map(Piece::pieceType).orElse(null) ==
                              PieceType.ROOK && !((Rook) rookField.piece()).hasMovedAtLeastOnce();
        boolean isInCheck = isInCheck(grid, from, from, opponentFactory, false);

        return isValidFrom && isValidTo && isKingValid && isRookValid && !isInCheck;
    }

    private boolean isInCheck(List<Field> grid, Field from, Field to, PieceLogicFactory opponentFactory,
                              boolean isOpponent) {
        if (isOpponent) {
            return false;
        }

        return grid
                .stream()
                .filter(opponentField -> opponentField.piece() != null)
                .filter(opponentField -> opponentField.piece().color() != from.piece().color())
                .anyMatch(opponentField -> opponentFactory
                        .getLogic(opponentField.piece().pieceType())
                        .isValidMove(grid, opponentField, to, opponentFactory, true));
    }
}
