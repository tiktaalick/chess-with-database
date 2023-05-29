package org.mark.chess.ai;

import lombok.Getter;
import lombok.Setter;
import org.mark.chess.rulesengine.RulesEngine;

/**
 * Contains the rules engine with which a chessboard value can be calculated.
 */
@Getter
@Setter
public final class ChessboardValueRulesEngine extends RulesEngine<ChessboardValueParameter, ChessboardValue> {

    public static final BestMove BEST_MOVE = new BestMove();

    private final ChessboardValue chessboardValue = new ChessboardValue();

    /**
     * Constructor that adds all the rules to the rules engine.
     */
    public ChessboardValueRulesEngine() {
        addRule(new TotalValueOfAllPiecesRule(chessboardValue));
        addRule(new TotalValueRule(chessboardValue));
    }
}
