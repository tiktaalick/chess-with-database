package org.mark.chess.ai;

import lombok.Getter;
import lombok.Setter;
import org.mark.chess.rulesengine.RulesEngine;

@Getter
@Setter
public class ChessboardValueRulesEngine extends RulesEngine<ChessboardValueParameter, ChessboardValue> {

    private final ChessboardValue chessboardValue = new ChessboardValue();

    public ChessboardValueRulesEngine() {
        getRules().add(new PieceValueRule(chessboardValue));
        getRules().add(new TotalValueRule(chessboardValue));
    }
}
