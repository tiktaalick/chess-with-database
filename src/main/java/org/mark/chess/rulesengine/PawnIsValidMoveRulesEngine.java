package org.mark.chess.rulesengine;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.isvalidmove.HasEmptyParametersRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsFriendlyFireRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsJumpingRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsMovingIntoCheckRule;
import org.mark.chess.rulesengine.rule.isvalidmove.IsNotValidRule;
import org.mark.chess.rulesengine.rule.isvalidmove.PawnIsNotValidDirectionRule;
import org.mark.chess.rulesengine.rule.isvalidmove.PawnIsValidBaselineMoveRule;
import org.mark.chess.rulesengine.rule.isvalidmove.PawnIsValidBasicMoveRule;
import org.mark.chess.rulesengine.rule.isvalidmove.PawnIsValidCaptureMoveRule;
import org.mark.chess.rulesengine.rule.isvalidmove.PawnIsValidEnPassantMoveRule;

public final class PawnIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public PawnIsValidMoveRulesEngine() {
        rules.add(new HasEmptyParametersRule());
        rules.add(new IsFriendlyFireRule());
        rules.add(new IsJumpingRule());
        rules.add(new IsMovingIntoCheckRule());
        rules.add(new PawnIsNotValidDirectionRule());
        rules.add(new PawnIsValidBasicMoveRule());
        rules.add(new PawnIsValidBaselineMoveRule());
        rules.add(new PawnIsValidCaptureMoveRule());
        rules.add(new PawnIsValidEnPassantMoveRule());
        rules.add(new IsNotValidRule());
    }
}
