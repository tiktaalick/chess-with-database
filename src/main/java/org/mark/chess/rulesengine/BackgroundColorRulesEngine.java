package org.mark.chess.rulesengine;

import org.mark.chess.model.Field;
import org.mark.chess.rulesengine.rule.backgroundcolor.AttackingRule;
import org.mark.chess.rulesengine.rule.backgroundcolor.CheckmateRule;
import org.mark.chess.rulesengine.rule.backgroundcolor.FieldValueRule;
import org.mark.chess.rulesengine.rule.backgroundcolor.HardwoodRule;
import org.mark.chess.rulesengine.rule.backgroundcolor.StalemateRule;

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
