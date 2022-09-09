package org.mark.chess.model;

import lombok.Data;
import org.mark.chess.logic.BishopLogic;
import org.mark.chess.logic.KingLogic;
import org.mark.chess.logic.KnightLogic;
import org.mark.chess.logic.PawnLogic;
import org.mark.chess.logic.QueenLogic;
import org.mark.chess.logic.RookLogic;
import org.springframework.stereotype.Component;

@Data
@Component
public class PieceTypeLogic {
    private final BishopLogic bishopLogic;
    private final KingLogic   kingLogic;
    private final KnightLogic knightLogic;
    private final PawnLogic   pawnLogic;
    private final QueenLogic  queenLogic;
    private final RookLogic   rookLogic;
}
