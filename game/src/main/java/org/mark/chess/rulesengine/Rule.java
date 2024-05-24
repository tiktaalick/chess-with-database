package org.mark.chess.rulesengine;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interface for rules that can be processed by a rules engine. Checks if the rule is applicable and creates a result.
 *
 * @param <T> The type of the object that will be used to process the rules.
 * @param <U> The type of the object that will be created.
 */
public interface Rule<T, U> {

    String getContext();

    Level getLogLevel();

    /**
     * Creates a result.
     *
     * @return The result.
     */
    U getResult();

    default Rule<T, U> logResult() {
        if (getLogLevel() != Level.OFF) {
            RuleLogger.LOGGER.log(getLogLevel(),
                    () -> this.getClass().getSimpleName() + " is true for " + getContext() + "; the result = " + this.getResult());
        }

        return this;
    }

    /**
     * Checks if the rule is applicable.
     *
     * @param ruleParameter The parameter that will be used by the rule.
     * @return True if applicable.
     */
    boolean stopProcessingfurtherRulesAndGetResultNow(T ruleParameter);

    final class RuleLogger {

        private static final Logger LOGGER = Logger.getLogger(RuleLogger.class.getName());

        private RuleLogger() { }
    }
}
