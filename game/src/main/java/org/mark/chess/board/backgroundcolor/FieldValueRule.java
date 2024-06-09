package org.mark.chess.board.backgroundcolor;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Field;
import org.mark.chess.rulesengine.Rule;

import java.awt.Color;
import java.util.logging.Level;

import static org.mark.chess.board.Chessboard.MAXIMUM_COLOR_VALUE;

public class FieldValueRule implements Rule<Field, Color> {

    private static final int    DOUBLE = 2;
    @Getter
    private              String context;
    private              Field  field  = new Field(null);

    @Override
    public Level getLogLevel() {
        return Level.OFF;
    }

    @Override
    public Color getResult() {
        return getValueColor(field);
    }

    @Override
    public boolean stopProcessingfurtherRulesAndGetResultNow(@NotNull Field field) {
        this.context = "field " + field.getCode();

        this.field = field;
        return field.isValidFrom() || field.isValidTo();
    }

    private static @NotNull Color getValueColor(@NotNull Field field) {
        int relativeValue = field.getRelativeValue() == null ? 0 : field.getRelativeValue().intValue();
        return new Color(MAXIMUM_COLOR_VALUE - relativeValue,
                Math.abs(MAXIMUM_COLOR_VALUE - DOUBLE * relativeValue),
                MAXIMUM_COLOR_VALUE - (Math.abs(MAXIMUM_COLOR_VALUE - DOUBLE * relativeValue)));
    }
}
