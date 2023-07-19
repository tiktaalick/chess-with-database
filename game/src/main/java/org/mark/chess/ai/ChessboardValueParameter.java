package org.mark.chess.ai;

import lombok.Data;
import org.mark.chess.board.Chessboard;
import org.mark.chess.player.PlayerColor;

/**
 * Contains the parameter to calculate the chessboard value with.
 */
@Data
public class ChessboardValueParameter {

    private final Chessboard  chessboard;
    private final PlayerColor activePlayerColor;
}
