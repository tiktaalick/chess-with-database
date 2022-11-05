package org.mark.chess.piece.isvalidmove;

import org.mark.chess.board.Field;
import org.mark.chess.board.Chessboard;
import org.mark.chess.piece.Pawn;
import org.mark.chess.rulesengine.Rule;

public class PawnIsValidEnPassantMoveRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    @Override
    public Boolean createResult() {
        return true;
    }

    @Override
    public boolean hasResult(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return isValidEnPassantMove(getGrid(), getFrom(), getTo());
    }

    private boolean isValidEnPassantMove(Chessboard chessboard, Field from, Field to) {
        return neighbourFieldsWithOpponentPawns(chessboard, from, from.getPieceType().getColor())
                .stream()
                .filter(field -> ((Pawn) field.getPieceType()).isMayBeCapturedEnPassant())
                .filter(field -> field.getCoordinates().getX() == to.getCoordinates().getX())
                .anyMatch(field -> getAbsoluteVerticalMove(field, to) == ONE_STEP);
    }
}
