package org.mark.chess.logic;

import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Field;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BishopLogic implements PieceLogic {
    @Autowired
    private GridLogic gridLogic;

    @Override
    public boolean isValidMove(List<Field> grid,
                               Field from,
                               Field to,
                               PieceLogicFactory opponentFactory,
                               boolean isOpponent) {
        return isValidBasicMove(from, to) &&
                !this.isFriendlyFire(from.piece(), to) &&
                !isJumping(grid, from, to) &&
                !isInCheck(grid, from, to, isOpponent, opponentFactory, gridLogic);

    }

    private boolean isValidBasicMove(Field from, Field to) {
        return getAbsoluteHorizontalMove(from, to) == getAbsoluteVerticalMove(from, to);
    }

}
