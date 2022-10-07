package org.mark.chess.board.backgroundcolor;

import org.mark.chess.board.Field;
import org.mark.chess.rulesengine.RulesEngine;

import java.awt.Color;

public final class BackgroundColorRulesEngine extends RulesEngine<Field, Color> {

    public BackgroundColorRulesEngine() {
        getRules().add(new CheckmateRule());
        getRules().add(new StalemateRule());
        getRules().add(new AttackingRule());
        getRules().add(new FieldValueRule());
        getRules().add(new HardwoodRule());
    }
}
