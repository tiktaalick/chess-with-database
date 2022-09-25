package org.mark.chess.piece.isvalidmove;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;

@Data
@Accessors(chain = true)
public class IsValidMoveParameter {

    private final Grid    grid;
    private final Field   from;
    private final Field   to;
    private final boolean isOpponent;
}
