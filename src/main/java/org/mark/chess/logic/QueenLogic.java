package org.mark.chess.logic;

import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class QueenLogic extends PieceLogic {

    @Lazy
    protected QueenLogic(CheckLogic checkLogic, GridLogic gridLogic) {
        super(checkLogic, gridLogic);
    }

    @Override
    public boolean isValidMove(Grid grid, Field from, Field to, boolean isOpponent) {
        return !hasEmptyParameters(grid, from, to) &&
                !this.isFriendlyFire(from.getPiece(), to) &&
                isValidQueenSpecificMove(grid, from, to) &&
                !checkLogic.isMovingIntoCheck(grid, from, to, isOpponent, gridLogic);
    }

    private static boolean isMoving(int horizontalMove, int verticalMove) {
        return !(horizontalMove == 0 && verticalMove == 0);
    }

    private static boolean isMovingDiagonally(int horizontalMove, int verticalMove) {
        return horizontalMove == verticalMove;
    }

    private static boolean isMovingHorizontally(int horizontalMove, int verticalMove) {
        return (horizontalMove != 0 && verticalMove == 0);
    }

    private static boolean isMovingVertically(int horizontalMove, int verticalMove) {
        return (horizontalMove == 0 && verticalMove != 0);
    }

    private boolean isValidBasicMove(Field from, Field to) {
        int horizontalMove = getAbsoluteHorizontalMove(from, to);
        int verticalMove = getAbsoluteVerticalMove(from, to);

        return isMoving(horizontalMove, verticalMove) &&
                (isMovingHorizontally(horizontalMove, verticalMove) ||
                        isMovingVertically(horizontalMove, verticalMove) ||
                        isMovingDiagonally(horizontalMove, verticalMove));
    }

    private boolean isValidQueenSpecificMove(Grid grid, Field from, Field to) {
        return !isJumping(grid, from, to) && isValidBasicMove(from, to);
    }
}
