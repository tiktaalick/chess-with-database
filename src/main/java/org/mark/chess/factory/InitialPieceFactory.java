package org.mark.chess.factory;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Bishop;
import org.mark.chess.model.King;
import org.mark.chess.model.Knight;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;

public class InitialPieceFactory {
    public Piece getInitialPiece(int id) {
        switch (id) {
            case 0:
            case 7:
                return new Rook().setColor(Color.BLACK);
            case 1:
            case 6:
                return new Knight().setColor(Color.BLACK);
            case 2:
            case 5:
                return new Bishop().setColor(Color.BLACK);
            case 3:
                return new Queen().setColor(Color.BLACK);
            case 4:
                return new King().setColor(Color.BLACK);
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                return new Pawn().setColor(Color.BLACK);
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
                return new Pawn().setColor(Color.WHITE);
            case 56:
            case 63:
                return new Rook().setColor(Color.WHITE);
            case 57:
            case 62:
                return new Knight().setColor(Color.WHITE);
            case 58:
            case 61:
                return new Bishop().setColor(Color.WHITE);
            case 59:
                return new Queen().setColor(Color.WHITE);
            case 60:
                return new King().setColor(Color.WHITE);
            default:
                return null;
        }
    }
}
