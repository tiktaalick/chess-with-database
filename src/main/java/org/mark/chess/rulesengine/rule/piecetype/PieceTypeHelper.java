package org.mark.chess.rulesengine.rule.piecetype;

import org.mark.chess.model.Field;

public class PieceTypeHelper {
    protected boolean isValidMove;

    protected int getAbsoluteHorizontalMove(Field from, Field to) {
        return Math.abs(to.getCoordinates().getX() - from.getCoordinates().getX());
    }

    protected int getAbsoluteVerticalMove(Field from, Field to) {
        return Math.abs(to.getCoordinates().getY() - from.getCoordinates().getY());
    }
}
