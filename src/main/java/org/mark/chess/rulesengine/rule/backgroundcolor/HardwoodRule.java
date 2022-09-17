package org.mark.chess.rulesengine.rule.backgroundcolor;

import org.mark.chess.model.Field;
import org.mark.chess.rulesengine.rule.Rule;

import java.awt.Color;

public class HardwoodRule implements Rule<Field, Color> {
    private static final int EVEN = 2;

    private Field field = new Field(null);

    @Override
    public Color create() {
        return (field.getCoordinates().getX() + field.getCoordinates().getY()) % EVEN == 0
                ? org.mark.chess.enums.Color.DARK.getAwtColor()
                : org.mark.chess.enums.Color.LIGHT.getAwtColor();
    }

    @Override
    public boolean test(Field field) {
        this.field = field;
        return true;
    }
}
