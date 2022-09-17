package org.mark.chess.rulesengine;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.piecetype.HasEmptyParametersRule;
import org.mark.chess.rulesengine.rule.piecetype.IsFriendlyFireRule;
import org.mark.chess.rulesengine.rule.piecetype.IsMovingIntoCheckRule;
import org.mark.chess.rulesengine.rule.piecetype.IsNotValidRule;
import org.mark.chess.rulesengine.rule.piecetype.KnightIsValidBasicMoveRule;

public final class KnightIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public KnightIsValidMoveRulesEngine() {
        rules.add(new HasEmptyParametersRule());
        rules.add(new IsFriendlyFireRule());
        rules.add(new IsMovingIntoCheckRule());
        rules.add(new KnightIsValidBasicMoveRule());
        rules.add(new IsNotValidRule());
    }
}
