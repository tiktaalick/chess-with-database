package org.mark.chess.piece.pawn.isvalidmove;

import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Field;
import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules;
import org.mark.chess.piece.pawn.Pawn;
import org.mark.chess.rulesengine.Rule;

public class PawnIsValidEnPassantMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    private IsValidMoveParameter isValidMoveParameter;

    @Override
    public String getContext() {
        return super.getContext(isValidMoveParameter);
    }

    @Override
    public Boolean getResult() {
        return true;
    }

    @Override
    public boolean stopProcessingfurtherRulesAndGetResultNow(IsValidMoveParameter isValidMoveParameter) {
        this.isValidMoveParameter = isValidMoveParameter;

        return isValidEnPassantMove(isValidMoveParameter.getChessboard(), isValidMoveParameter.getFrom(), isValidMoveParameter.getTo());
    }

    private boolean isValidEnPassantMove(Chessboard chessboard, Field from, Field to) {
        return neighbourFieldsWithOpponentPawns(chessboard, from, from.getPieceType().getColor())
                .stream()
                .filter(field -> ((Pawn) field.getPieceType()).isMayBeCapturedEnPassant())
                .filter(field -> field.getCoordinates().getX() == to.getCoordinates().getX())
                .anyMatch(field -> getAbsoluteVerticalMove(field, to) == ONE_STEP);
    }
}
