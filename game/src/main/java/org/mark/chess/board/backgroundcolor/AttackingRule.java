package org.mark.chess.board.backgroundcolor;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Field;
import org.mark.chess.rulesengine.Rule;

import java.awt.Color;
import java.util.logging.Level;

import static org.mark.chess.board.backgroundcolor.BackgroundColor.ATTACKING;

public class AttackingRule implements Rule<Field, Color> {

    @Getter
    private String context;

    @Override
    public Level getLogLevel() {
        return Level.OFF;
    }

    @Override
    public Color getResult() {
        return ATTACKING.getAwtColor();
    }

    @Override
    public boolean stopProcessingfurtherRulesAndGetResultNow(@NotNull Field field) {
        this.context = "field " + field.getCode();

        return (field.isAttacking() || field.isUnderAttack()) && !field.isValidFrom();
    }
}
