package org.mark.chess.board.backgroundcolor;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Field;
import org.mark.chess.rulesengine.Rule;

import java.awt.Color;

import static org.mark.chess.board.backgroundcolor.BackgroundColor.CHECKMATE;

public class CheckmateRule implements Rule<Field, Color> {

    @Override
    public Color createResult() {
        return CHECKMATE.getAwtColor();
    }

    @Override
    public boolean hasResult(@NotNull Field field) {
        return field.isCheckMate();
    }
}
