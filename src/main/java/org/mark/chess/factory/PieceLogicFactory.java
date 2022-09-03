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
import org.springframework.stereotype.Component;

@Component
public class PieceLogicFactory {
    private BishopLogic bishopLogic;
    private KingLogic   kingLogic;
    private KnightLogic knightLogic;
    private PawnLogic   pawnLogic;
    private QueenLogic  queenLogic;
    private RookLogic   rookLogic;

    @Autowired
    public PieceLogicFactory(BishopLogic bishopLogic,
            KingLogic kingLogic,
            KnightLogic knightLogic,
            PawnLogic pawnLogic,
            QueenLogic queenLogic,
            RookLogic rookLogic) {
        this.bishopLogic = bishopLogic;
        this.kingLogic = kingLogic;
        this.knightLogic = knightLogic;
        this.pawnLogic = pawnLogic;
        this.queenLogic = queenLogic;
        this.rookLogic = rookLogic;
    }

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
