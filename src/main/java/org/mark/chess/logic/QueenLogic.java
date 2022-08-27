package org.mark.chess.logic;

import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class QueenLogic implements PieceLogic {
    private GridLogic  gridLogic;
    private CheckLogic checkLogic;

    @Lazy
    @Autowired
    public QueenLogic(GridLogic gridLogic, CheckLogic checkLogic) {
        this.gridLogic = gridLogic;
        this.checkLogic = checkLogic;
    }

    @Override
    public boolean isValidMove(Grid grid, Field from, Field to, boolean isOpponent) {
        return !hasEmptyParameters(grid, from, to) &&
               isValidBasicMove(from, to) &&
               !this.isFriendlyFire(from.getPiece(), to) &&
               !isJumping(grid, from, to) &&
               !checkLogic.isMovingIntoCheck(grid, from, to, isOpponent, gridLogic);
    }

    private boolean isValidBasicMove(Field from, Field to) {
        int horizontalMove = getAbsoluteHorizontalMove(from, to);
        int verticalMove = getAbsoluteVerticalMove(from, to);
        return ((horizontalMove != 0 && verticalMove == 0) || (horizontalMove == 0 && verticalMove != 0) || horizontalMove == verticalMove) &&
               !(horizontalMove == 0 && verticalMove == 0);
    }
}
