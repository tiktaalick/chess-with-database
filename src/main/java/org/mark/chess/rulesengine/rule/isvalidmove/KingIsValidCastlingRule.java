package org.mark.chess.rulesengine.rule.isvalidmove;

import org.mark.chess.enums.PieceType;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.King;
import org.mark.chess.model.Piece;
import org.mark.chess.model.Rook;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.Rule;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class KingIsValidCastlingRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {
    public static final  int KING_X_LEFT       = 3;
    public static final  int KING_X_RIGHT      = 7;
    public static final  int ROOK_X_LEFT_FROM  = 1;
    public static final  int ROOK_X_LEFT_TO    = 4;
    public static final  int ROOK_X_RIGHT_FROM = 8;
    public static final  int ROOK_X_RIGHT_TO   = 6;
    private static final int KING_INITIAL_X    = 5;

    public static boolean isValidCastling(Grid grid, Field from, Field to, int direction, boolean isOpponent, boolean isNowCastling) {

        return !isOpponent &&
                isValidCastlingPositions(from, to, direction) &&
                isValidCastlingPieces(grid, from, direction, isNowCastling) &&
                !from.isInCheckNow(grid, false);
    }

    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean test(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return isValidCastling(getGrid(), getFrom(), getTo(), KING_X_LEFT, isOpponent(), false) ||
                isValidCastling(getGrid(), getFrom(), getTo(), KING_X_RIGHT, isOpponent(), false);
    }

    private static Field getRookField(Grid grid, Field from, int direction) {
        return grid.getField(new Coordinates((direction == KING_X_LEFT
                ? ROOK_X_LEFT_FROM
                : ROOK_X_RIGHT_FROM), from.getPiece().getColor().getBaseline()));
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

    private static boolean isValidCastlingPieces(Grid grid, Field from, int direction, boolean isNowCastling) {
        return isKingValid(from, isNowCastling) && isRookValid(getRookField(grid, from, direction));
    }

    private static boolean isValidCastlingPositions(Field from, Field to, int direction) {
        return isValidFrom(from) && isValdiTo(from, to, direction);
    }

    private static boolean isValidFrom(Field from) {
        return from.getCoordinates().getX() == KING_INITIAL_X && from.getCoordinates().getY() == from.getPiece().getColor().getBaseline();
    }
}
