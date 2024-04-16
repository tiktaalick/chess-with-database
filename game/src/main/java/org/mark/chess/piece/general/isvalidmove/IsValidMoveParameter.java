package org.mark.chess.piece.general.isvalidmove;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Field;

@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain = true)
public class IsValidMoveParameter {

    private final Chessboard chessboard;
    private final Field      from;
    private final Field      to;
    private final boolean    isOpponent;
}
