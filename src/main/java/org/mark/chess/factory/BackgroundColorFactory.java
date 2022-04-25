package org.mark.chess.factory;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;

public class BackgroundColorFactory {
    public java.awt.Color getBackgroundColor(Field field) {
        if (field.isAttacking()) {
            return Color.ATTACKING.getAwtColor();
        } else if (field.isValidMove()) {
            return Color.VALID_MOVE.getAwtColor();
        } else {
            return (field.getCoordinates().getX() + field.getCoordinates().getY()) % 2 == 0
                    ? Color.LIGHT.getAwtColor()
                    : Color.DARK.getAwtColor();
        }
    }
}
