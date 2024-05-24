package org.mark.chess.ai;

import org.mark.chess.rulesengine.Rule;

import java.util.logging.Level;

/**
 * A rule that calculates the total value of the current chessboard position.
 */
public class ChessboardValueResultRule implements Rule<ChessboardValueParameter, ChessboardValue> {

    private final ChessboardValue chessboardValue;

    /**
     * Constructor that sets the chessboard value.
     *
     * @param chessboardValue The chessboard value.
     */
    public ChessboardValueResultRule(ChessboardValue chessboardValue) { this.chessboardValue = chessboardValue; }

    @Override
    public String getContext() {
        return "result";
    }

    @Override
    public Level getLogLevel() {
        return Level.OFF;
    }

    @Override
    public ChessboardValue getResult() {
        return chessboardValue;
    }

    @Override
    public boolean stopProcessingfurtherRulesAndGetResultNow(ChessboardValueParameter ruleParameter) {
        chessboardValue.setTotalValue(chessboardValue.getTotalPieceValue());

        return true;
    }
}
