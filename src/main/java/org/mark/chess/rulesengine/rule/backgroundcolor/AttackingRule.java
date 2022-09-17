package org.mark.chess.rulesengine.rule.backgroundcolor;

import org.mark.chess.model.Field;
import org.mark.chess.rulesengine.rule.Rule;

import java.awt.Color;

import static org.mark.chess.enums.Color.ATTACKING;

public class AttackingRule implements Rule<Field, Color> {
    @Override
    public Color create() {
        return ATTACKING.getAwtColor();
    }

    @Override
    public boolean test(Field field) {
        return (field.isAttacking() || field.isUnderAttack()) && !field.isValidFrom();
    }
}
