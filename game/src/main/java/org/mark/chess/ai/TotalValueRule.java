package org.mark.chess.ai;

import org.mark.chess.rulesengine.Rule;

public class TotalValueRule implements Rule<ChessboardValueParameter, ChessboardValue> {

    private final ChessboardValue chessboardValue;

    public TotalValueRule(ChessboardValue chessboardValue) { this.chessboardValue = chessboardValue; }

    @Override
    public ChessboardValue create() {
        return chessboardValue;
    }

    @Override
    public boolean isApplicable(ChessboardValueParameter ruleParameter) {
        chessboardValue.setTotalValue(chessboardValue.getPieceValue());

        return true;
    }
}
