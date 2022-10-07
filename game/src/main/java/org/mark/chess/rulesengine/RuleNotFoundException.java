package org.mark.chess.rulesengine;

/**
 * An {@link IllegalArgumentException} that will be thrown if none of the rules is applicable.
 */
public class RuleNotFoundException extends IllegalArgumentException {

    /**
     * Constructor for {@link RuleNotFoundException}.
     */
    public RuleNotFoundException() {
        super("Rule not found.");
    }
}
