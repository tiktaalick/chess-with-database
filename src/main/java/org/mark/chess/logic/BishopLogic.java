package org.mark.chess.logic;

import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;

import static org.mark.chess.enums.PieceType.BISHOP;

@Service
public class BishopLogic extends PieceLogic {

    @Lazy
    protected BishopLogic(CheckLogic checkLogic, GridLogic gridLogic) {
        super(checkLogic, gridLogic);
    }

    @Override
    public boolean isValidMove(Grid grid, Field from, Field to, boolean isOpponent) {
        return BISHOP.isValidMove(new IsValidMoveParameter(grid, from, to, checkLogic, gridLogic, isOpponent));
    }
}
