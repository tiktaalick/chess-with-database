package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

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
