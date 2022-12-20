package org.mark.chess.ai;

import java.util.function.Function;

/**
 * Contains a method that searches for the best move.
 */
public class BestMove {

    protected static final int NUMBER_OF_MOVES_AHEAD = 2;
    private static final   int EVEN                  = 2;

    /**
     * Searches for the best move.
     *
     * @param typeOfCalculation        The type of calculation used to search for the best move.
     * @param chessboardValueParameter A container for chessboard value parameters.
     * @param level                    The number of levels still to go in the search for the best move.
     * @return The calculated value.
     */
    public int calculate(Function<ChessboardValueParameter, Integer> typeOfCalculation,
            ChessboardValueParameter chessboardValueParameter,
            int level) {
        if (level == 0 /* || the game has ended*/) {
            return typeOfCalculation.apply(chessboardValueParameter);
        }

        var returnValue = typeOfCalculation.apply(chessboardValueParameter);

//        if (isActivePlayerMove(level)) {
//            returnValue = Integer.MIN_VALUE;
//
////            List<Chessboard> chessboardChildren =
//
//        } else {
//            returnValue = Integer.MAX_VALUE;
//        }
        return returnValue;
    }

    private static boolean isActivePlayerMove(int level) {
        return level % EVEN == 0;
    }

//    function  minimax( node, depth, maximizingPlayer ) is
//    if depth = 0 or node is a terminal node then
//        return the heuristic value of node
//    if maximizingPlayer then
//    value := −∞
//            for each child of node do
//    value := max( value, minimax( child, depth − 1, FALSE ) )
//            return value
//    else (* minimizing player *)
//    value := +∞
//            for each child of node do
//    value := min( value, minimax( child, depth − 1, TRUE ) )
//            return value
}
