package org.mark.chess.ai;

import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Contains a method that searches for the best move.
 */
public class BestMove {

    private static final Logger LOGGER = Logger.getLogger(BestMove.class.getName());

    /**
     * Searches for the best move.
     *
     * @param typeOfCalculation        The type of calculation used to search for the best move.
     * @param chessboardValueParameter A container for chessboard value parameters.
     * @return The calculated value.
     */
    public int calculate(Function<ChessboardValueParameter, Integer> typeOfCalculation, ChessboardValueParameter chessboardValueParameter) {
        int returnValue;

        returnValue = typeOfCalculation.apply(chessboardValueParameter);

        return returnValue;
    }
}
