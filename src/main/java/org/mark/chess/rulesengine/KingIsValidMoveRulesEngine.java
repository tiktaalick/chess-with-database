package org.mark.chess.rulesengine;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsJumpingRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsNotValidRule;
import org.mark.chess.rulesengine.rule.isvalidmove.KingIsValidBasicMoveRule;
import org.mark.chess.rulesengine.rule.isvalidmove.KingIsValidCastlingRule;

public final class KingIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public KingIsValidMoveRulesEngine() {
        rules.add(new HasEmptyParametersRule());
        rules.add(new IsFriendlyFireRule());
        rules.add(new IsJumpingRule());
        rules.add(new IsMovingIntoCheckRule());
        rules.add(new KingIsValidBasicMoveRule());
        rules.add(new KingIsValidCastlingRule());
        rules.add(new IsNotValidRule());
    }
}
