package org.mark.chess.ai;

import lombok.Data;

/**
 * Contains the combination of values that are related to a chessboard position.
 */
@Data
public class ChessboardValue {

    private int pieceValue;
    private int totalValue;
}
