package org.mark.chess.logic;

import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.springframework.beans.factory.annotation.Autowired;

public class KnightLogic implements PieceLogic {
    @Autowired
    private GridLogic gridLogic;

    @Override
    public boolean isValidMove(Grid grid, Field from, Field to, PieceLogicFactory opponentFactory, boolean isOpponent) {
        return !hasEmptyParameters(grid, from, to, opponentFactory) &&
               isValidBasicMove(from, to) &&
               !this.isFriendlyFire(from.getPiece(), to) &&
               !isMovingIntoCheck(grid, from, to, isOpponent, opponentFactory, gridLogic);
    }

    private boolean isValidBasicMove(Field from, Field to) {
        int horizontalMove = getAbsoluteHorizontalMove(from, to);
        int verticalMove = getAbsoluteVerticalMove(from, to);
        return (horizontalMove == 1 && verticalMove == 2) || (horizontalMove == 2 && verticalMove == 1);
    }
}
