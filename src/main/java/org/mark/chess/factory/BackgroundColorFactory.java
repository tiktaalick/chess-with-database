package org.mark.chess.factory;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.model.Field;
import org.mark.chess.rule.backgroundcolor.BackgroundColorRulesEngine;

public final class BackgroundColorFactory {
    public static final int MAX_COLOR_VALUE = 255;
    public static final int MIN_COLOR_VALUE = 0;

    private static final BackgroundColorRulesEngine backgroundColorRulesEngine = new BackgroundColorRulesEngine();

    private BackgroundColorFactory() {
    }

    public static java.awt.Color getBackgroundColor(@NotNull Field field) {

        return backgroundColorRulesEngine.process(field);
    }
}
