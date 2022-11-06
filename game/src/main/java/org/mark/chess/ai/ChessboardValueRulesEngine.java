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

    private final ChessboardValue chessboardValue = new ChessboardValue();

    /**
     * Constructor that adds all the rules to the rules engine.
     */
    public ChessboardValueRulesEngine() {
        addRule(new PieceValueRule(chessboardValue));
        addRule(new TotalValueRule(chessboardValue));
    }
}
