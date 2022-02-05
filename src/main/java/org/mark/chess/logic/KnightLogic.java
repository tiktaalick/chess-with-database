package org.mark.chess.logic;

import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Field;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class KnightLogic implements PieceLogic {
    @Autowired
    private GridLogic gridLogic;

    @Override
    public boolean isValidMove(List<Field> grid,
                               Field from,
                               Field to,
                               PieceLogicFactory opponentFactory,
                               boolean isOpponent) {
        return !this.isFriendlyFire(from.piece(), to) &&
                !isInCheck(grid, from, to, isOpponent, opponentFactory, gridLogic) &&
                isValidBasicMove(from, to);
    }

    private boolean isValidBasicMove(Field from, Field to) {
        int horizontalMove = getAbsoluteHorizontalMove(from, to);
        int verticalMove = getAbsoluteVerticalMove(from, to);
        return (horizontalMove == 1 && verticalMove == 2) ||
                (horizontalMove == 2 && verticalMove == 1);
    }

}
