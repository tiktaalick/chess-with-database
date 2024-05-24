package org.mark.chess.board.backgroundcolor;

import lombok.Getter;
import org.mark.chess.board.Field;
import org.mark.chess.rulesengine.Rule;

import java.awt.Color;
import java.util.logging.Level;

public class HardwoodRule implements Rule<Field, Color> {

    private static final int    EVEN  = 2;
    @Getter
    private              String context;
    private              Field  field = new Field(null);

    @Override
    public Level getLogLevel() {
        return Level.OFF;
    }

    @Override
    public Color getResult() {
        return (field.getCoordinates().getX() + field.getCoordinates().getY()) % EVEN == 0
               ? BackgroundColor.DARK.getAwtColor()
               : BackgroundColor.LIGHT.getAwtColor();
    }

    @Override
    public boolean stopProcessingfurtherRulesAndGetResultNow(Field field) {
        this.context = "field " + field.getCode();

        this.field = field;
        return true;
    }
}
