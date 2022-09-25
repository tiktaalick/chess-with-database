package org.mark.chess.piece;

import java.util.HashMap;
import java.util.Map;

import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

public final class InitialPieceRepository {

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

    private static final Map<Integer, PieceType> initialPieces;

    static {
        initialPieces = new HashMap<>();
        initialPieces.put(FIELD_A1, new Rook(WHITE));
        initialPieces.put(FIELD_B1, new Knight(WHITE));
        initialPieces.put(FIELD_C1, new Bishop(WHITE));
        initialPieces.put(FIELD_D1, new Queen(WHITE));
        initialPieces.put(FIELD_E1, new King(WHITE));
        initialPieces.put(FIELD_F1, new Bishop(WHITE));
        initialPieces.put(FIELD_G1, new Knight(WHITE));
        initialPieces.put(FIELD_H1, new Rook(WHITE));
        initialPieces.put(FIELD_A8, new Rook(BLACK));
        initialPieces.put(FIELD_B8, new Knight(BLACK));
        initialPieces.put(FIELD_C8, new Bishop(BLACK));
        initialPieces.put(FIELD_D8, new Queen(BLACK));
        initialPieces.put(FIELD_E8, new King(BLACK));
        initialPieces.put(FIELD_F8, new Bishop(BLACK));
        initialPieces.put(FIELD_G8, new Knight(BLACK));
        initialPieces.put(FIELD_H8, new Rook(BLACK));
    }

    private InitialPieceRepository() {
    }

    public static PieceType getInitialPiece(int id) {
        if (isBlackPawnId(id)) {
            return new Pawn(BLACK);
        } else if (isWhitePawnId(id)) {
            return new Pawn(WHITE);
        } else {
            return initialPieces.get(id);
        }
    }

    private static boolean isBlackPawnId(int id) {
        return id >= FIELD_A7 && id <= FIELD_H7;
    }

    private static boolean isWhitePawnId(int id) {
        return id >= FIELD_A2 && id <= FIELD_H2;
    }
}
