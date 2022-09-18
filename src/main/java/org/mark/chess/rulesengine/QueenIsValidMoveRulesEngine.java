package org.mark.chess.rulesengine;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsJumpingRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsNotValidRule;
import org.mark.chess.rulesengine.rule.isvalidmove.QueenIsValidBasicMoveRule;

public final class QueenIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public QueenIsValidMoveRulesEngine() {
        rules.add(new HasEmptyParametersRule());
        rules.add(new IsFriendlyFireRule());
        rules.add(new IsJumpingRule());
        rules.add(new IsMovingIntoCheckRule());
        rules.add(new QueenIsValidBasicMoveRule());
        rules.add(new IsNotValidRule());
    }
}
