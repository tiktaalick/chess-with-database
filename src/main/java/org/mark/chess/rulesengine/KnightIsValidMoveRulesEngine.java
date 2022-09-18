package org.mark.chess.rulesengine;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsNotValidRule;
import org.mark.chess.rulesengine.rule.isvalidmove.KnightIsValidBasicMoveRule;

public final class KnightIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public KnightIsValidMoveRulesEngine() {
        rules.add(new HasEmptyParametersRule());
        rules.add(new IsFriendlyFireRule());
        rules.add(new IsMovingIntoCheckRule());
        rules.add(new KnightIsValidBasicMoveRule());
        rules.add(new IsNotValidRule());
    }
}
