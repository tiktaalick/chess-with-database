package org.mark.chess.ai;

import org.mark.chess.board.Chessboard;
import org.mark.chess.board.ChessboardDirector;

import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains a method that searches for the best move.
 */
public class BestMove {

    public static final  int    NUMBER_OF_MOVES_AHEAD = 1;
    private static final int    EVEN                  = 2;
    private static final Logger LOGGER                = Logger.getLogger(BestMove.class.getName());

    /**
     * Searches for the best move.
     *
     * @param typeOfCalculation        The type of calculation used to search for the best move.
     * @param chessboardValueParameter A container for chessboard value parameters.
     * @return The calculated value.
     */
    public int calculate(Function<ChessboardValueParameter, Integer> typeOfCalculation, ChessboardValueParameter chessboardValueParameter) {
        int returnValue;

        var parentChessboard = chessboardValueParameter.getChessboard();
        var activePlayerColor = chessboardValueParameter.getActivePlayerColor();

        if (parentChessboard.getLevel() <= 0 /* || the game has ended*/) {
            returnValue = typeOfCalculation.apply(chessboardValueParameter);
        } else {
            LOGGER.log(Level.INFO, "BestMove.calculate(): {0}", "");
            LOGGER.log(Level.INFO, "BestMove.calculate(): {0}", "level             = " + parentChessboard.getLevel());
            LOGGER.log(Level.INFO, "BestMove.calculate(): {0}", "activePlayerColor = " + activePlayerColor.getName());

            var activePlayerColorLevel = isActivePlayerMove(parentChessboard.getLevel()) ? activePlayerColor : activePlayerColor.getOpposite();

            LOGGER.log(Level.INFO, "BestMove.calculate(): {0}", "activePlayerColor = " + activePlayerColorLevel + " (for this level)");
            LOGGER.log(Level.INFO, "BestMove.calculate(): {0}", "parentChessboard  = " + parentChessboard);
            LOGGER.log(Level.INFO, "BestMove.calculate(): {0}", parentChessboard.getFromParentToChildMove());

            List<Chessboard> chessboardChildren = ChessboardDirector.createChessboardChildren(parentChessboard, activePlayerColorLevel);

            LOGGER.log(Level.INFO, "BestMove.calculate(): {0}", "number of children= " + chessboardChildren.size());

            returnValue = chessboardChildren
                    .stream()
                    .peek(child -> LOGGER.log(Level.INFO, "BestMove.calculate(): {0}", "childChessboard   = " + child))
                    .mapToInt(chessboard -> calculate(typeOfCalculation, new ChessboardValueParameter(chessboard, activePlayerColorLevel)))
                    .max()
                    .orElse(Integer.MIN_VALUE);

            LOGGER.log(Level.INFO, "BestMove.calculate(): {0}", "returnValue       = " + returnValue);
            LOGGER.log(Level.INFO, "BestMove.calculate(): {0}", "");
        }

        return returnValue;
    }

    private static boolean isActivePlayerMove(int level) {
        return level % EVEN != 0;
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
