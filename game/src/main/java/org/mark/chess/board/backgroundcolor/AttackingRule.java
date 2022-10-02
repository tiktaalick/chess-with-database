package org.mark.chess.board.backgroundcolor;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Field;
import org.mark.chess.rulesengine.Rule;

import java.awt.Color;

import static org.mark.chess.board.backgroundcolor.BackgroundColor.ATTACKING;

public class AttackingRule implements Rule<Field, Color> {

    @Override
    public Color create() {
        return ATTACKING.getAwtColor();
    }

    @Override
    public boolean isApplicable(@NotNull Field field) {
        return (field.isAttacking() || field.isUnderAttack()) && !field.isValidFrom();
    }
}
