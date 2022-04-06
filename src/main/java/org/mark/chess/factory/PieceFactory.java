package org.mark.chess.factory;

import org.mark.chess.enums.PieceType;
import org.mark.chess.model.Bishop;
import org.mark.chess.model.King;
import org.mark.chess.model.Knight;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;

public class PieceFactory {
    public Piece getPiece(PieceType pieceType) {
        switch (pieceType) {
            case KING:
                return new King();
            case QUEEN:
                return new Queen();
            case ROOK:
                return new Rook();
            case BISHOP:
                return new Bishop();
            case KNIGHT:
                return new Knight();
            case PAWN:
                return new Pawn();
            default:
                return null;
        }
    }
}
