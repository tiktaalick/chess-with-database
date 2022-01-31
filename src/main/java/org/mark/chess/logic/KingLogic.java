package org.mark.chess.logic;

import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Field;

import java.util.Arrays;
import java.util.List;

public class KingLogic implements PieceLogic {

    @Override
    public boolean isValidMove(List<Field> grid,
                               Field from,
                               Field to,
                               PieceLogicFactory opponentFactory,
                               boolean isOpponent) {
        return !this.isFriendlyFire(from.piece(), to) &&
                !isJumping(grid, from, to) &&
                isValidBasicMove(from, to) &&
                !isInCheck(grid, from, to, isOpponent, opponentFactory);
    }

    private boolean isValidBasicMove(Field from, Field to) {
        return Arrays.asList(0, 1).contains(getAbsoluteHorizontalMove(from, to)) &&
                Arrays.asList(0, 1).contains(getAbsoluteVerticalMove(from, to));
    }

    private boolean isInCheck(List<Field> grid, Field from, Field to, boolean isOpponent,
                              PieceLogicFactory opponentFactory) {
        if (isOpponent) {
            return false;
        }

        return grid.stream()
                .filter(opponentField -> opponentField.piece() != null)
                .filter(opponentField -> opponentField.piece().color() != from.piece().color())
                .anyMatch(opponentField -> opponentFactory.getLogic(opponentField.piece().pieceType())
                        .isValidMove(grid, opponentField, to, opponentFactory, true));
    }
}
