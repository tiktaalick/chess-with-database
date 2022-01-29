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
        int x = Math.abs(to.coordinates().x() - from.coordinates().x());
        int y = Math.abs(to.coordinates().y() - from.coordinates().y());
        return Arrays.asList(0, 1).contains(x) && Arrays.asList(0, 1).contains(y);
    }

    private boolean isInCheck(List<Field> grid, Field from, Field to, boolean isOpponent,
                              PieceLogicFactory opponentFactory) {
        if (isOpponent) {
            return false;
        }

        return grid.stream()
                .filter(field -> field.piece() != null)
                .filter(field -> field.piece().color() != from.piece().color())
                .anyMatch(opponentField -> opponentFactory.getLogic(opponentField.piece().pieceType())
                        .isValidMove(grid, opponentField, to, opponentFactory, true));
    }
}
