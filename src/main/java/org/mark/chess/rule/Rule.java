package org.mark.chess.rule;

import java.util.function.Predicate;

public interface Rule<T, U> extends Predicate<T> {
    U create();
}

