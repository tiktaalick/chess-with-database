package org.mark.chess.ai;

import org.mark.chess.rulesengine.Rule;

/**
 * A rule that calculates the total value of the current chessboard position.
 */
public class TotalValueRule implements Rule<ChessboardValueParameter, ChessboardValue> {

    private final ChessboardValue chessboardValue;

    /**
     * Constructor that sets the chessboard value.
     *
     * @param chessboardValue The chessboard value.
     */
    public TotalValueRule(ChessboardValue chessboardValue) { this.chessboardValue = chessboardValue; }

    @Override
    public ChessboardValue createResult() {
        return chessboardValue;
    }

    @Override
    public boolean hasResult(ChessboardValueParameter ruleParameter) {
        chessboardValue.setTotalValue(chessboardValue.getPieceValue());

        return true;
    }
}
