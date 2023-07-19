package org.mark.chess.rulesengine;

/**
 * Interface for rules that can be processed by a rules engine. Checks if the rule is applicable and creates a result.
 *
 * @param <T> The type of the object that will be used to process the rules.
 * @param <U> The type of the object that will be created.
 */
public interface Rule<T, U> {

    /**
     * Creates a result.
     *
     * @return The result.
     */
    U createResult();

    /**
     * Checks if the rule is applicable.
     *
     * @param ruleParameter The parameter that will be used by the rule.
     * @return True if applicable.
     */
    boolean hasResult(T ruleParameter);
}

