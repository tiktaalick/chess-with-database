package org.mark.chess.ai;

import lombok.Getter;
import lombok.Setter;
import org.mark.chess.rulesengine.RulesEngine;

@Getter
@Setter
public final class ChessboardValueRulesEngine extends RulesEngine<ChessboardValueParameter, ChessboardValue> {

    private final ChessboardValue chessboardValue = new ChessboardValue();

    public ChessboardValueRulesEngine() {
        addRule(new PieceValueRule(chessboardValue));
        addRule(new TotalValueRule(chessboardValue));
    }
}
