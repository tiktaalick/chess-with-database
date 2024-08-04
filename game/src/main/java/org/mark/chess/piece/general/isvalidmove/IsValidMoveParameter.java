package org.mark.chess.piece.general.isvalidmove;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Field;

import static org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules.ZERO_STEPS;
import static org.mark.chess.piece.general.isvalidmove.PieceTypeSharedRules.isCaptureMove;

@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain = true)
public class IsValidMoveParameter {

    private static final int TWO_STEPS_FORWARD_FROM_BASELINE = 2;

    private final Chessboard chessboard;
    private final Field      from;
    private final Field      to;
    private final boolean    isOpponent;

    public int getAbsoluteHorizontalMove() {
        return PieceTypeSharedRules.getAbsoluteHorizontalMove(from, to);
    }

    public int getAbsoluteVerticalMove() {
        return PieceTypeSharedRules.getAbsoluteVerticalMove(from, to);
    }

    public boolean isDiagonalMove() {
        return getAbsoluteHorizontalMove() != ZERO_STEPS && getAbsoluteHorizontalMove() == getAbsoluteVerticalMove();
    }

    public boolean isHorizontalMove() {
        return getAbsoluteHorizontalMove() != ZERO_STEPS && getAbsoluteVerticalMove() == ZERO_STEPS;
    }

    public boolean isVerticalMove() {
        return getAbsoluteHorizontalMove() == ZERO_STEPS && getAbsoluteVerticalMove() != ZERO_STEPS;
    }

    public boolean pawnIsValidBaselineMove() {
        return !isCaptureMove(getFrom(), getTo()) &&
                getFrom().getPieceType().getColor().getBaselinePawn() == getFrom().getCoordinates().getY() &&
                PieceTypeSharedRules.getAbsoluteHorizontalMove(getFrom(), getTo()) == 0 &&
                PieceTypeSharedRules.getAbsoluteVerticalMove(getFrom(), getTo()) == TWO_STEPS_FORWARD_FROM_BASELINE;
    }
}
