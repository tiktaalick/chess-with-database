package org.mark.chess.logic;

import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Field;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RookLogic implements PieceLogic {
    @Autowired
    private FieldLogic fieldLogic;

    @Override
    public boolean isValidMove(List<Field> grid,
                               Field from,
                               Field to,
                               PieceLogicFactory opponentFactory,
                               boolean isOpponent) {
        return !this.isFriendlyFire(from.piece(), to) &&
                !isJumping(grid, from, to) &&
                !isInCheck(grid, from, to, isOpponent, opponentFactory, fieldLogic) &&
                isValidBasicMove(from, to);
    }

    private boolean isValidBasicMove(Field from, Field to) {
        int horizontalMove = getAbsoluteHorizontalMove(from, to);
        int verticalMove = getAbsoluteVerticalMove(from, to);
        return (horizontalMove != 0 && verticalMove == 0) ||
                (horizontalMove == 0 && verticalMove != 0);
    }

}
