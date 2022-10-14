package org.mark.chess.rulesengine;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Parent class for the more specific rules engines. Processes a list of rules in order, searches for the first applicable rule and then creates a
 * result. If none of the rules is applicable a {@link RuleNotFoundException} will be thrown.
 *
 * @param <T> The type of the object that will be used to process the rules.
 * @param <U> The type of the object that will be created.
 */
@Data
public class RulesEngine<T, U> {

    private List<Rule<T, U>> rules = new ArrayList<>();

    /**
     * Processes a list of rules in order and searches for the first applicable rule.
     *
     * @param ruleParameter The parameter that will be used to process the rules.
     * @return The created result for the first applicable rule.
     */
    public U process(T ruleParameter) {
        return rules.stream().filter(rule -> rule.isApplicable(ruleParameter)).findFirst().orElseThrow(RuleNotFoundException::new).create();
    }
}
