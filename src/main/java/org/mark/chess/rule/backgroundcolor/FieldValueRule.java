package org.mark.chess.rule.backgroundcolor;

import org.mark.chess.model.Field;
import org.mark.chess.rule.Rule;

import java.awt.Color;

import static org.mark.chess.factory.BackgroundColorFactory.MAX_COLOR_VALUE;

public class FieldValueRule implements Rule<Field, Color> {
    private static final int   DOUBLE = 2;
    private              Field field  = new Field(null);

    @Override
    public Color create() {
        return getValueColor(field);
    }

    @Override
    public boolean test(Field field) {
        this.field = field;
        return field.isValidFrom() || field.isValidMove();
    }

    private static Color getValueColor(Field field) {
        int relativeValue = field.getRelativeValue() == null
                ? 0
                : field.getRelativeValue();
        return new Color(MAX_COLOR_VALUE - relativeValue,
                Math.abs(MAX_COLOR_VALUE - DOUBLE * relativeValue),
                MAX_COLOR_VALUE - (Math.abs(MAX_COLOR_VALUE - DOUBLE * relativeValue)));
    }
}
