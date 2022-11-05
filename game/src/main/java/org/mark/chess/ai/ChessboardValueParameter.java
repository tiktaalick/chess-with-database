package org.mark.chess.ai;

import lombok.Data;
import org.mark.chess.board.Chessboard;
import org.mark.chess.player.PlayerColor;

@Data
public class ChessboardValueParameter {

    private final Chessboard  chessboard;
    private final PlayerColor activePlayerColor;
}
