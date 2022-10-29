package org.mark.chess.piece.isvalidmove;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.board.Chessboard;
import org.mark.chess.piece.King;
import org.mark.chess.piece.PieceType;
import org.mark.chess.piece.Rook;
import org.mark.chess.player.PlayerColor;
import org.mark.chess.rulesengine.Rule;

import java.util.Arrays;
import java.util.Optional;

import static org.mark.chess.piece.PieceType.QUEEN;
import static org.mark.chess.piece.PieceType.ROOK;

public class KingIsValidCastlingRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    public static final  int KING_CASTLING_TO_THE_LEFT    = 3;
    public static final  int KING_CASTLING_TO_THE_RIGHT   = 7;
    public static final  int ROOK_CASTLING_FROM_THE_LEFT  = 1;
    public static final  int ROOK_CASTLING_FROM_THE_RIGHT = 8;
    public static final  int ROOK_CASTLING_TO_THE_LEFT    = 6;
    public static final  int ROOK_CASTLING_TO_THE_RIGHT   = 4;
    private static final int KING_STARTING_POSITION       = 5;

    public static boolean isValidCastling(Chessboard chessboard, Field from, Field to, int direction, boolean isOpponent, boolean isNowCastling) {

        return !isOpponent &&
                isValidCastlingPositions(from, to, direction) &&
                isValidCastlingPieces(chessboard, from, direction, isNowCastling) &&
                !from.isInCheckNow(chessboard, false);
    }

    @Override
    public Boolean create() {
        return true;
    }

    @Override
    public boolean isApplicable(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return isValidCastling(getGrid(), getFrom(), getTo(), KING_CASTLING_TO_THE_LEFT, isOpponent(), false) ||
                isValidCastling(getGrid(), getFrom(), getTo(), KING_CASTLING_TO_THE_RIGHT, isOpponent(), false);
    }

    private static Field getRookField(@NotNull Chessboard chessboard, @NotNull Field from, int direction) {
        return chessboard.getField(new Coordinates((direction == KING_CASTLING_TO_THE_LEFT
                ? ROOK_CASTLING_FROM_THE_LEFT
                : ROOK_CASTLING_FROM_THE_RIGHT), from.getPieceType().getColor().getBaseline()));
    }

    private static boolean isKingValid(Field from, boolean isNowCastling) {
        return isNowCastling || !((King) from.getPieceType()).isHasMovedAtLeastOnce();
    }

    private static boolean isRookValid(Field rookField) {
        return Optional.ofNullable(rookField).map(Field::getPieceType).map(PieceType::getName).orElse(QUEEN).equals(ROOK) &&
                !((Rook) Optional.of(rookField).map(Field::getPieceType).orElse(new Rook(PlayerColor.BLACK))).isHasMovedAtLeastOnce();
    }

    private static boolean isValidCastlingPieces(Chessboard chessboard, Field from, int direction, boolean isNowCastling) {
        return isKingValid(from, isNowCastling) && isRookValid(getRookField(chessboard, from, direction));
    }

    private static boolean isValidCastlingPositions(Field from, Field to, int direction) {
        return isValidFrom(from) && isValidTo(from, to, direction);
    }

    private static boolean isValidFrom(@NotNull Field from) {
        return from.getCoordinates().getX() == KING_STARTING_POSITION && from.getCoordinates().getY() == from.getPieceType().getColor().getBaseline();
    }

    private static boolean isValidTo(Field from, Field to, int direction) {
        return !isCaptureMove(from, to) &&
                Arrays.asList(KING_CASTLING_TO_THE_LEFT, KING_CASTLING_TO_THE_RIGHT).contains(direction) &&
                to.getCoordinates().getX() == direction &&
                to.getCoordinates().getY() == from.getPieceType().getColor().getBaseline();
    }
}
