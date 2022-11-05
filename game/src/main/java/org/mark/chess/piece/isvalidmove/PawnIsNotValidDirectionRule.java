package org.mark.chess.piece.isvalidmove;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Field;
import org.mark.chess.rulesengine.Rule;

import static org.mark.chess.player.PlayerColor.WHITE;

public class PawnIsNotValidDirectionRule extends PieceTypeSharedRules implements Rule<IsValidMoveParameter, Boolean> {

    private static final int GOING_DOWN = -1;
    private static final int GOING_UP   = 1;

    @Override
    public Boolean createResult() {
        return false;
    }

    @Override
    public boolean hasResult(IsValidMoveParameter isValidMoveParameter) {
        setParameter(isValidMoveParameter);

        return !isValidDirection(getFrom(), getTo());
    }

    private static boolean isValidDirection(@NotNull Field from, @NotNull Field to) {
        return Integer.signum(to.getCoordinates().getY() - from.getCoordinates().getY()) ==
                (from.getPieceType().getColor() == WHITE
                        ? GOING_UP
                        : GOING_DOWN);
    }
}
