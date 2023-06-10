package org.mark.chess.ai;

import org.mark.chess.board.Chessboard;
import org.mark.chess.player.PlayerColor;
import org.mark.chess.rulesengine.Rule;

import java.util.logging.Logger;

import static org.mark.chess.ai.ChessboardValueRulesEngine.BEST_MOVE;

/**
 * A rule that calculates the value of a board position based upon the piece values.
 */
public class TotalValueOfAllPiecesRule implements Rule<ChessboardValueParameter, ChessboardValue> {

    private static final Logger                LOGGER             = Logger.getLogger(TotalValueOfAllPiecesRule.class.getName());
    private static final TotalValueOfAllPieces TOTAL_PIECES_VALUE = new TotalValueOfAllPieces();

    private final ChessboardValue chessboardValue;

    /**
     * Constructor.
     *
     * @param chessboardValue The chessboard value.
     */
    public TotalValueOfAllPiecesRule(ChessboardValue chessboardValue) {
        this.chessboardValue = chessboardValue;
    }

    @Override
    public ChessboardValue createResult() {
        return chessboardValue;
    }

    /**
     * Stores the total of all piece values combined and returns false.
     *
     * @param chessboardValueParameter A parameter that contains a {@link Chessboard} and a {@link PlayerColor}.
     * @return Returns false. Only the {@link TotalValueRule} will eventually return with a result.
     */
    @Override
    public boolean hasResult(ChessboardValueParameter chessboardValueParameter) {
        chessboardValue.setPieceValue(BEST_MOVE.calculate(TOTAL_PIECES_VALUE, chessboardValueParameter));

        return false;
    }
}
