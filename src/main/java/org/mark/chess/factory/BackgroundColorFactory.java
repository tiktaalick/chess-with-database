package org.mark.chess.factory;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;

public final class BackgroundColorFactory {
    public static final  int MAX_COLOR_VALUE = 255;
    public static final  int MIN_COLOR_VALUE = 0;
    private static final int DOUBLE          = 2;
    private static final int EVEN            = 2;

    private BackgroundColorFactory() {
    }

    public static java.awt.Color getBackgroundColor(@NotNull Field field) {
        if (field.isCheckMate()) {
            return Color.CHECKMATE.getAwtColor();
        } else if (field.isStaleMate()) {
            return Color.STALEMATE.getAwtColor();
        } else if ((field.isAttacking() || field.isUnderAttack()) && !field.isValidFrom()) {
            return Color.ATTACKING.getAwtColor();
        } else if (field.isValidFrom() || field.isValidMove()) {
            return getValueColor(field);
        } else {
            return (field.getCoordinates().getX() + field.getCoordinates().getY()) % EVEN == 0
                    ? Color.DARK.getAwtColor()
                    : Color.LIGHT.getAwtColor();
        }
    }

    private static java.awt.Color getValueColor(Field field) {
        int relativeValue = field.getRelativeValue() == null
                ? 0
                : field.getRelativeValue();
        return new java.awt.Color(MAX_COLOR_VALUE - relativeValue,
                Math.abs(MAX_COLOR_VALUE - DOUBLE * relativeValue),
                MAX_COLOR_VALUE - (Math.abs(MAX_COLOR_VALUE - DOUBLE * relativeValue)));
    }
}
