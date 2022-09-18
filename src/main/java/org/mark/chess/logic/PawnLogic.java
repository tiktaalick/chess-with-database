package org.mark.chess.logic;

import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static org.mark.chess.enums.PieceType.PAWN;

@Service
public class PawnLogic extends PieceLogic {
    @Lazy
    protected PawnLogic(CheckLogic checkLogic, GridLogic gridLogic) {
        super(checkLogic, gridLogic);
    }

    @Override
    public boolean isValidMove(Grid grid, Field from, Field to, boolean isOpponent) {
        return PAWN.isValidMove(new IsValidMoveParameter(grid, from, to, checkLogic, gridLogic, isOpponent));
    }
}
