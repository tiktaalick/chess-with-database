package org.mark.chess.rulesengine;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.piecetype.HasEmptyParametersRule;
import org.mark.chess.rulesengine.rule.piecetype.IsFriendlyFireRule;
import org.mark.chess.rulesengine.rule.piecetype.IsJumpingRule;
import org.mark.chess.rulesengine.rule.piecetype.IsMovingIntoCheckRule;
import org.mark.chess.rulesengine.rule.piecetype.IsNotValidRule;
import org.mark.chess.rulesengine.rule.piecetype.QueenIsValidBasicMoveRule;

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
