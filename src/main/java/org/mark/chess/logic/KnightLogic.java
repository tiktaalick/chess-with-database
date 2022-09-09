package org.mark.chess.logic;

import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class KnightLogic extends PieceLogic {

    private static final int ONE_STEP  = 1;
    private static final int TWO_STEPS = 2;

    @Lazy
    protected KnightLogic(CheckLogic checkLogic, GridLogic gridLogic) {
        super(checkLogic, gridLogic);
    }

    @Override
    public boolean isValidMove(Grid grid, Field from, Field to, boolean isOpponent) {
        return !hasEmptyParameters(grid, from, to) &&
                isValidBasicMove(from, to) &&
                !this.isFriendlyFire(from.getPiece(), to) &&
                !checkLogic.isMovingIntoCheck(grid, from, to, isOpponent, gridLogic);
    }

    private boolean isValidBasicMove(Field from, Field to) {
        int horizontalMove = getAbsoluteHorizontalMove(from, to);
        int verticalMove = getAbsoluteVerticalMove(from, to);
        return (horizontalMove == ONE_STEP && verticalMove == TWO_STEPS) || (horizontalMove == TWO_STEPS && verticalMove == ONE_STEP);
    }
}
