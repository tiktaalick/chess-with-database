package org.mark.chess.factory;

import org.mark.chess.enums.PieceType;
import org.mark.chess.logic.KingLogic;
import org.mark.chess.logic.PieceLogic;
import org.springframework.beans.factory.annotation.Autowired;

public class PieceLogicFactory {
    @Autowired
    KingLogic kingLogic;

    public PieceLogic getLogic(PieceType pieceType) {
        switch (pieceType) {
            case KING:
                return kingLogic;
            case QUEEN:
                return kingLogic;
            case ROOK:
                return kingLogic;
            case BISHOP:
                return kingLogic;
            case KNIGHT:
                return kingLogic;
            case PAWN:
                return kingLogic;
            default:
                return kingLogic;
        }
    }
}
