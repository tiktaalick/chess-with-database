package org.mark.chess.rulesengine;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.piecetype.BishopIsValidBasicMoveRule;
import org.mark.chess.rulesengine.rule.piecetype.IsEmptyParametersRule;
import org.mark.chess.rulesengine.rule.piecetype.IsFriendlyFireRule;
import org.mark.chess.rulesengine.rule.piecetype.IsJumpingRule;
import org.mark.chess.rulesengine.rule.piecetype.IsMovingIntoCheckRule;
import org.mark.chess.rulesengine.rule.piecetype.IsNotValidRule;

public final class BishopIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public BishopIsValidMoveRulesEngine() {
        rules.add(new IsEmptyParametersRule());
        rules.add(new IsFriendlyFireRule());
        rules.add(new IsJumpingRule());
        rules.add(new IsMovingIntoCheckRule());
        rules.add(new BishopIsValidBasicMoveRule());
        rules.add(new IsNotValidRule());
    }
}
