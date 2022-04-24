package org.mark.chess.factory;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;

public class BackgroundColorFactory {
    public java.awt.Color getBackgroundColor(Field field) {
        java.awt.Color woodColor = (field.getCoordinates().getX() + field.getCoordinates().getY()) % 2 == 0
                ? Color.LIGHT.getAwtColor()
                : Color.DARK.getAwtColor();

        return field.isValidMove()
                ? Color.VALID_MOVE.getAwtColor()
                : woodColor;
    }
}
