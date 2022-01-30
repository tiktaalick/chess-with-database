package org.mark.chess.logic;

import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Field;

import java.util.List;

public class BishopLogic implements PieceLogic {

    @Override
    public boolean isValidMove(List<Field> grid,
                               Field from,
                               Field to,
                               PieceLogicFactory opponentFactory,
                               boolean isOpponent) {
        return !this.isFriendlyFire(from.piece(), to) &&
                !isJumping(grid, from, to) &&
                isValidBasicMove(from, to);
    }

    private boolean isValidBasicMove(Field from, Field to) {
        return getAbsoluteHorizontalMove(from, to) == getAbsoluteVerticalMove(from, to);
    }

}
