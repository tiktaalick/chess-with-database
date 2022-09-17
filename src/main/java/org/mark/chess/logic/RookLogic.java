package org.mark.chess.logic;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class RookLogic extends PieceLogic {

    @Lazy
    protected RookLogic(CheckLogic checkLogic, GridLogic gridLogic) {
        super(checkLogic, gridLogic);
    }

    @Override
    public boolean isValidMove(Grid grid, Field from, Field to, boolean isOpponent) {
        return !hasEmptyParameters(grid, from, to) &&
                isValidBasicMove(from, to) &&
                !isFriendlyFire(from.getPiece(), to) &&
                !isJumping(grid, from, to) &&
                !checkLogic.isMovingIntoCheck(grid, from, to, isOpponent, gridLogic);
    }

    private boolean isValidBasicMove(Field from, Field to) {
        int horizontalMove = getAbsoluteHorizontalMove(from, to);
        int verticalMove = getAbsoluteVerticalMove(from, to);
        return (horizontalMove != 0 && verticalMove == 0) || (horizontalMove == 0 && verticalMove != 0);
    }
}
