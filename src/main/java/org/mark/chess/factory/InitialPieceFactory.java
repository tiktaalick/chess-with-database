package org.mark.chess.factory;

import org.mark.chess.model.Bishop;
import org.mark.chess.model.King;
import org.mark.chess.model.Knight;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;

import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;

public class InitialPieceFactory {
    public Piece getInitialPiece(int id) {
        switch (id) {
            case 0:
            case 7:
                return new Rook().setColor(BLACK);
            case 1:
            case 6:
                return new Knight().setColor(BLACK);
            case 2:
            case 5:
                return new Bishop().setColor(BLACK);
            case 3:
                return new Queen().setColor(BLACK);
            case 4:
                return new King().setColor(BLACK);
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                return new Pawn().setColor(BLACK);
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
                return new Pawn().setColor(WHITE);
            case 56:
            case 63:
                return new Rook().setColor(WHITE);
            case 57:
            case 62:
                return new Knight().setColor(WHITE);
            case 58:
            case 61:
                return new Bishop().setColor(WHITE);
            case 59:
                return new Queen().setColor(WHITE);
            case 60:
                return new King().setColor(WHITE);
            default:
                return null;
        }
    }
}
