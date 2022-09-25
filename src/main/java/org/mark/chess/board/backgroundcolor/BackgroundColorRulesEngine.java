package org.mark.chess.board.backgroundcolor;

import org.mark.chess.board.Field;
import org.mark.chess.rulesengine.RulesEngine;

import java.awt.Color;

public final class BackgroundColorRulesEngine extends RulesEngine<Field, Color> {

    public BackgroundColorRulesEngine() {
        rules.add(new CheckmateRule());
        rules.add(new StalemateRule());
        rules.add(new AttackingRule());
        rules.add(new FieldValueRule());
        rules.add(new HardwoodRule());
    }
}