package org.mark.chess.factory;

import org.mark.chess.model.Bishop;
import org.mark.chess.model.King;
import org.mark.chess.model.Knight;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;

import java.util.Arrays;

import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;

public final class InitialPieceFactory {

    private static final int FIELD_A1 = 56;
    private static final int FIELD_A2 = 48;
    private static final int FIELD_A7 = 8;
    private static final int FIELD_A8 = 0;
    private static final int FIELD_B1 = 57;
    private static final int FIELD_B8 = 1;
    private static final int FIELD_C1 = 58;
    private static final int FIELD_C8 = 2;
    private static final int FIELD_D1 = 59;
    private static final int FIELD_D8 = 3;
    private static final int FIELD_E1 = 60;
    private static final int FIELD_E8 = 4;
    private static final int FIELD_F1 = 61;
    private static final int FIELD_F8 = 5;
    private static final int FIELD_G1 = 62;
    private static final int FIELD_G8 = 6;
    private static final int FIELD_H1 = 63;
    private static final int FIELD_H2 = 55;
    private static final int FIELD_H7 = 15;
    private static final int FIELD_H8 = 7;

    private InitialPieceFactory() {
    }

    public static Piece getInitialPiece(int id) {
        if (isBlackRookId(id)) {
            return new Rook().setColor(BLACK);
        } else if (isBlackKnightId(id)) {
            return new Knight().setColor(BLACK);
        } else if (isBlackBishopId(id)) {
            return new Bishop().setColor(BLACK);
        } else if (isBlackQueenId(id)) {
            return new Queen().setColor(BLACK);
        } else if (isBlackKingId(id)) {
            return new King().setColor(BLACK);
        } else if (isBlackPawnId(id)) {
            return new Pawn().setColor(BLACK);
        } else if (isWhitePawnId(id)) {
            return new Pawn().setColor(WHITE);
        } else if (isWhiteRookId(id)) {
            return new Rook().setColor(WHITE);
        } else if (isWhiteKnightId(id)) {
            return new Knight().setColor(WHITE);
        } else if (isWhiteBishopId(id)) {
            return new Bishop().setColor(WHITE);
        } else if (isWhiteQueenId(id)) {
            return new Queen().setColor(WHITE);
        } else if (isWhiteKingId(id)) {
            return new King().setColor(WHITE);
        } else {
            return null;
        }
    }

    private static boolean isBlackBishopId(int id) {
        return Arrays.asList(FIELD_C8, FIELD_F8).contains(id);
    }

    private static boolean isBlackKingId(int id) {
        return id == FIELD_E8;
    }

    private static boolean isBlackKnightId(int id) {
        return Arrays.asList(FIELD_B8, FIELD_G8).contains(id);
    }

    private static boolean isBlackPawnId(int id) {
        return id >= FIELD_A7 && id <= FIELD_H7;
    }

    private static boolean isBlackQueenId(int id) {
        return id == FIELD_D8;
    }

    private static boolean isBlackRookId(int id) {
        return Arrays.asList(FIELD_A8, FIELD_H8).contains(id);
    }

    private static boolean isWhiteBishopId(int id) {
        return Arrays.asList(FIELD_C1, FIELD_F1).contains(id);
    }

    private static boolean isWhiteKingId(int id) {
        return id == FIELD_E1;
    }

    private static boolean isWhiteKnightId(int id) {
        return Arrays.asList(FIELD_B1, FIELD_G1).contains(id);
    }

    private static boolean isWhitePawnId(int id) {
        return id >= FIELD_A2 && id <= FIELD_H2;
    }

    private static boolean isWhiteQueenId(int id) {
        return id == FIELD_D1;
    }

    private static boolean isWhiteRookId(int id) {
        return Arrays.asList(FIELD_A1, FIELD_H1).contains(id);
    }
}