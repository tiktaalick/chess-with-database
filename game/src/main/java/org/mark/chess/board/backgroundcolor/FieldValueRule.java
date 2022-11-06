package org.mark.chess.board.backgroundcolor;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Field;
import org.mark.chess.rulesengine.Rule;

import java.awt.Color;

import static org.mark.chess.game.Game.MAX_COLOR_VALUE;

public class FieldValueRule implements Rule<Field, Color> {

    private static final int   DOUBLE = 2;
    private              Field field  = new Field(null);

    @Override
    public Color createResult() {
        return getValueColor(field);
    }

    @Override
    public boolean hasResult(@NotNull Field field) {
        this.field = field;
        return field.isValidFrom() || field.hasValidTo();
    }

    private static @NotNull Color getValueColor(@NotNull Field field) {
        int relativeValue = field.getRelativeValue() == null
                ? 0
                : field.getRelativeValue();
        return new Color(MAX_COLOR_VALUE - relativeValue,
                Math.abs(MAX_COLOR_VALUE - DOUBLE * relativeValue),
                MAX_COLOR_VALUE - (Math.abs(MAX_COLOR_VALUE - DOUBLE * relativeValue)));
    }
}
