package org.mark.chess.rule.backgroundcolor;

import org.mark.chess.model.Field;
import org.mark.chess.rule.Rule;

import java.awt.Color;

import static org.mark.chess.enums.Color.CHECKMATE;

public class CheckmateRule implements Rule<Field, Color> {

    @Override
    public Color create() {
        return CHECKMATE.getAwtColor();
    }

    @Override
    public boolean test(Field field) {
        return field.isCheckMate();
    }
}
