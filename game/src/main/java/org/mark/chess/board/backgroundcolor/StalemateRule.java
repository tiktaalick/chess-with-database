package org.mark.chess.board.backgroundcolor;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Field;
import org.mark.chess.rulesengine.Rule;

import java.awt.Color;

import static org.mark.chess.board.backgroundcolor.BackgroundColor.STALEMATE;

public class StalemateRule implements Rule<Field, Color> {

    @Override
    public Color create() {
        return STALEMATE.getAwtColor();
    }

    @Override
    public boolean isApplicable(@NotNull Field field) {
        return field.isStaleMate();
    }
}
