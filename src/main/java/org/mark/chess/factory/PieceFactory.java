package org.mark.chess.factory;

import org.mark.chess.enums.PieceType;
import org.mark.chess.model.*;

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
