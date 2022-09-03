package org.mark.chess.factory;

import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.model.Bishop;
import org.mark.chess.model.King;
import org.mark.chess.model.Knight;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;

public final class PieceFactory {
    private PieceFactory() {
    }

    public static Piece getPiece(PieceType pieceType, Color color) {
        switch (pieceType) {
            case KING:
                return new King(color);
            case ROOK:
                return new Rook(color);
            case BISHOP:
                return new Bishop(color);
            case KNIGHT:
                return new Knight(color);
            case PAWN:
                return new Pawn(color);
            case QUEEN:
            default:
                return new Queen(color);
        }
    }
}
