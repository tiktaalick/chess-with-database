package org.mark.chess.factory;

import org.mark.chess.enums.PieceType;
import org.mark.chess.logic.BishopLogic;
import org.mark.chess.logic.KingLogic;
import org.mark.chess.logic.KnightLogic;
import org.mark.chess.logic.PawnLogic;
import org.mark.chess.logic.PieceLogic;
import org.mark.chess.logic.QueenLogic;
import org.mark.chess.logic.RookLogic;
import org.springframework.beans.factory.annotation.Autowired;

public class PieceLogicFactory {
    @Autowired
    private KingLogic kingLogic;

    @Autowired
    private QueenLogic queenLogic;

    @Autowired
    private RookLogic rookLogic;

    @Autowired
    private BishopLogic bishopLogic;

    @Autowired
    private KnightLogic knightLogic;

    @Autowired
    private PawnLogic pawnLogic;

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
                return null;
        }
    }
}
