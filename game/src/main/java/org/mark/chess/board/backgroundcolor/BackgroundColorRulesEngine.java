package org.mark.chess.board.backgroundcolor;

import org.mark.chess.board.Field;
import org.mark.chess.rulesengine.RulesEngine;

import java.awt.Color;

public final class BackgroundColorRulesEngine extends RulesEngine<Field, Color> {

    /**
     * Initializes the rules for this engine.
     */
    public BackgroundColorRulesEngine() {
        addRule(new CheckmateRule());
        addRule(new StalemateRule());
        addRule(new AttackingRule());
        addRule(new FieldValueRule());
        addRule(new HardwoodRule());
    }
}
