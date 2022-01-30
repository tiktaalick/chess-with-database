package org.mark.chess.factory;

import org.mark.chess.enums.PieceType;
import org.mark.chess.logic.*;
import org.springframework.beans.factory.annotation.Autowired;

public class PieceLogicFactory {
    @Autowired
    KingLogic kingLogic;

    @Autowired
    QueenLogic queenLogic;

    @Autowired
    RookLogic rookLogic;

    @Autowired
    BishopLogic bishopLogic;

    @Autowired
    KnightLogic knightLogic;

    @Autowired
    PawnLogic pawnLogic;

    public PieceLogic getLogic(PieceType pieceType) {
        switch (pieceType) {
            case KING:
                return kingLogic;
            case QUEEN:
                return queenLogic;
            case ROOK:
                return rookLogic;
            case BISHOP:
                return bishopLogic;
            case KNIGHT:
                return knightLogic;
            case PAWN:
                return pawnLogic;
            default:
                return kingLogic;
        }
    }
}
