package org.mark.chess.rule;

import java.util.ArrayList;
import java.util.List;

public class RulesEngine<T, U> {
    protected List<Rule<T, U>> rules = new ArrayList<>();

    public U process(T t) {
        return rules.stream().filter(rule -> rule.test(t)).findFirst().orElseThrow(() -> new IllegalArgumentException("Rule not found.")).create();
    }
}
