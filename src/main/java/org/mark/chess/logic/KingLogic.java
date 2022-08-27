package org.mark.chess.logic;

import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.King;
import org.mark.chess.model.Piece;
import org.mark.chess.model.Rook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class KingLogic implements PieceLogic {
    static final         int KING_X_LEFT       = 3;
    static final         int KING_X_RIGHT      = 7;
    static final         int ROOK_X_LEFT_FROM  = 1;
    static final         int ROOK_X_LEFT_TO    = 4;
    static final         int ROOK_X_RIGHT_FROM = 8;
    static final         int ROOK_X_RIGHT_TO   = 6;
    private static final int KING_INITIAL_X    = 5;

    private GridLogic         gridLogic;
    private CheckLogic        checkLogic;
    private PieceLogicFactory pieceLogicFactory;

    @Autowired
    @Lazy
    public KingLogic(GridLogic gridLogic, CheckLogic checkLogic, PieceLogicFactory pieceLogicFactory) {
        this.gridLogic = gridLogic;
        this.checkLogic = checkLogic;
        this.pieceLogicFactory = pieceLogicFactory;
    }

    private static boolean isKingValid(Field from, boolean isNowCastling) {
        return isNowCastling || !((King) from.getPiece()).isHasMovedAtLeastOnce();
    }

    private static boolean isRookValid(Field rookField) {
        return Optional.ofNullable(rookField).map(Field::getPiece).map(Piece::getPieceType).orElse(null) == PieceType.ROOK &&
               !((Rook) rookField.getPiece()).isHasMovedAtLeastOnce();
    }

    private static boolean isValdiTo(Field from, Field to, int direction) {
        return Arrays.asList(KING_X_LEFT, KING_X_RIGHT).contains(direction) &&
               to.getCoordinates().getX() == direction &&
               to.getCoordinates().getY() == from.getPiece().getColor().getBaseline();
    }

    private static boolean isValidFrom(Field from) {
        return from.getCoordinates().getX() == KING_INITIAL_X && from.getCoordinates().getY() == from.getPiece().getColor().getBaseline();
    }

    @Override
    public boolean isValidMove(Grid grid, Field from, Field to, boolean isOpponent) {
        return !hasEmptyParameters(grid, from, to) &&
               (isValidBasicMove(from, to) ||
                isValidCastling(grid, from, to, KING_X_LEFT, isOpponent, false) ||
                isValidCastling(grid, from, to, KING_X_RIGHT, isOpponent, false)) &&
               !isFriendlyFire(from.getPiece(), to) &&
               !isJumping(grid, from, to) &&
               !checkLogic.isMovingIntoCheck(grid, from, to, isOpponent, gridLogic);
    }

    boolean isValidCastling(Grid grid, Field from, Field to, int direction, boolean isOpponent, boolean isNowCastling) {

        return !isOpponent &&
               isValidFrom(from) &&
               isValdiTo(from, to, direction) &&
               isKingValid(from, isNowCastling) &&
               isRookValid(getRookField(grid, from, direction)) &&
               !checkLogic.isInCheckNow(grid, from, from, false);
    }

    private Field getRookField(Grid grid, Field from, int direction) {
        return gridLogic.getField(grid,
                new Coordinates((direction == KING_X_LEFT
                        ? ROOK_X_LEFT_FROM
                        : ROOK_X_RIGHT_FROM), from.getPiece().getColor().getBaseline()));
    }

    private boolean isValidBasicMove(Field from, Field to) {
        int horizontalMove = getAbsoluteHorizontalMove(from, to);
        int verticalMove = getAbsoluteVerticalMove(from, to);
        return Arrays.asList(0, 1).contains(horizontalMove) &&
               Arrays.asList(0, 1).contains(verticalMove) &&
               !(horizontalMove == 0 && verticalMove == 0);
    }
}
