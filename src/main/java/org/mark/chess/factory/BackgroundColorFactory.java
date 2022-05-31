package org.mark.chess.factory;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;

public class BackgroundColorFactory {
    public java.awt.Color getBackgroundColor(Field field) {
        if (field.isCheckMate()) {
            return Color.CHECKMATE.getAwtColor();
        } else if (field.isStaleMate()) {
            return Color.STALEMATE.getAwtColor();
        } else if (field.isAttacking()) {
            return Color.ATTACKING.getAwtColor();
        } else if (field.isValidFrom()) {
            return Color.VALID_FROM.getAwtColor();
        } else if (field.isValidMove()) {
            return getValueColor(field);
        } else {
            return (field.getCoordinates().getX() + field.getCoordinates().getY()) % 2 == 0
                    ? Color.DARK.getAwtColor()
                    : Color.LIGHT.getAwtColor();
        }
    }

    private java.awt.Color getValueColor(Field field) {
        return new java.awt.Color(255 - field.getRelativeValue(), field.getRelativeValue(), 255 - (Math.abs(255 - 2 * field.getRelativeValue())));
    }
}
