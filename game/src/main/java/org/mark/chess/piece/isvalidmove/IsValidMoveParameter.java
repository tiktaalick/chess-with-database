package org.mark.chess.piece.isvalidmove;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;

@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain = true)
public class IsValidMoveParameter {

    private final Grid    grid;
    private final Field   from;
    private final Field   to;
    private final boolean isOpponent;
}
