package org.mark.chess.board.backgroundcolor;

import org.mark.chess.board.Field;
import org.mark.chess.rulesengine.Rule;

import java.awt.Color;

public class HardwoodRule implements Rule<Field, Color> {

    private static final int EVEN = 2;

    private Field field = new Field(null);

    @Override
    public Color createResult() {
        return (field.getCoordinates().getX() + field.getCoordinates().getY()) % EVEN == 0
                ? BackgroundColor.DARK.getAwtColor()
                : BackgroundColor.LIGHT.getAwtColor();
    }

    @Override
    public boolean hasResult(Field field) {
        this.field = field;
        return true;
    }
}
