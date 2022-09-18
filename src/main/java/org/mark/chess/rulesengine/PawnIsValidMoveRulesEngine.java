package org.mark.chess.rulesengine;

import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.piecetype.HasEmptyParametersRule;
import org.mark.chess.rulesengine.rule.piecetype.IsFriendlyFireRule;
import org.mark.chess.rulesengine.rule.piecetype.IsJumpingRule;
import org.mark.chess.rulesengine.rule.piecetype.IsMovingIntoCheckRule;
import org.mark.chess.rulesengine.rule.piecetype.IsNotValidRule;
import org.mark.chess.rulesengine.rule.piecetype.PawnIsNotValidDirectionRule;
import org.mark.chess.rulesengine.rule.piecetype.PawnIsValidBaselineMoveRule;
import org.mark.chess.rulesengine.rule.piecetype.PawnIsValidBasicMoveRule;
import org.mark.chess.rulesengine.rule.piecetype.PawnIsValidCaptureMoveRule;
import org.mark.chess.rulesengine.rule.piecetype.PawnIsValidEnPassantMoveRule;

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
