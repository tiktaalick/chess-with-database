package org.mark.chess.piece.isvalidmove;

import org.mark.chess.rulesengine.RulesEngine;

public final class PawnIsValidMoveRulesEngine extends RulesEngine<IsValidMoveParameter, Boolean> {

    public PawnIsValidMoveRulesEngine() {
        getRules().add(new HasEmptyParametersRule());
        getRules().add(new IsFriendlyFireRule());
        getRules().add(new IsJumpingRule());
        getRules().add(new IsMovingIntoCheckRule());
        getRules().add(new PawnIsNotValidDirectionRule());
        getRules().add(new PawnIsValidBasicMoveRule());
        getRules().add(new PawnIsValidBaselineMoveRule());
        getRules().add(new PawnIsValidCaptureMoveRule());
        getRules().add(new PawnIsValidEnPassantMoveRule());
        getRules().add(new IsNotValidRule());
    }
}
