package org.mark.chess.factory;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;

public class BackgroundColorFactory {
    public static final int MAX_COLOR_VALUE = 255;
    public static final int MIN_COLOR_VALUE = 0;

    public java.awt.Color getBackgroundColor(Field field) {
        if (field.isCheckMate()) {
            return Color.CHECKMATE.getAwtColor();
        } else if (field.isStaleMate()) {
            return Color.STALEMATE.getAwtColor();
        } else if ((field.isAttacking() || field.isUnderAttack()) && !field.isValidFrom()) {
            return Color.ATTACKING.getAwtColor();
        } else if (field.isValidFrom() || field.isValidMove()) {
            return getValueColor(field);
        } else {
            return (field.getCoordinates().getX() + field.getCoordinates().getY()) % 2 == 0
                    ? Color.DARK.getAwtColor()
                    : Color.LIGHT.getAwtColor();
        }
    }

    private java.awt.Color getValueColor(Field field) {
        int relativeValue = field.getRelativeValue() == null
                ? 0
                : field.getRelativeValue();
        return new java.awt.Color(MAX_COLOR_VALUE - relativeValue,
                Math.abs(MAX_COLOR_VALUE - 2 * relativeValue),
                MAX_COLOR_VALUE - (Math.abs(MAX_COLOR_VALUE - 2 * relativeValue)));
    }
}
