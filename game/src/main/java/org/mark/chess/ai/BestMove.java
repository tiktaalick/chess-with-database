package org.mark.chess.ai;

import org.mark.chess.board.Chessboard;
import org.mark.chess.board.ChessboardDirector;
import org.mark.chess.board.Field;
import org.mark.chess.move.Move;
import org.mark.chess.piece.PieceType;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Contains a method that searches for the best move.
 */
public class BestMove {

    public static final  int NUMBER_OF_MOVES_AHEAD = 1;
    private static final int EVEN                  = 2;

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
            System.out.println("");
            System.out.println("level             = " + parentChessboard.getLevel());
            System.out.println("activePlayerColor = " + activePlayerColor.getName());

            var activePlayerColorLevel = isActivePlayerMove(parentChessboard.getLevel())
                    ? activePlayerColor
                    : activePlayerColor.getOpposite();

            System.out.println("activePlayerColor = " + activePlayerColorLevel + " (for this level)");
            System.out.println("parentChessboard  = " + parentChessboard);
            System.out.println("Move              = " +
                    Optional
                            .ofNullable(parentChessboard.getFromParentToChildMove())
                            .map(Move::getFrom)
                            .map(Field::getPieceType)
                            .map(PieceType::getColor)
                            .orElse(null) +
                    " " +
                    Optional
                            .ofNullable(parentChessboard.getFromParentToChildMove())
                            .map(Move::getFrom)
                            .map(Field::getPieceType)
                            .map(PieceType::getName)
                            .orElse(null) +
                    " " +
                    Optional.ofNullable(parentChessboard.getFromParentToChildMove()).map(Move::getFrom).map(Field::getCode).orElse(null) +
                    " - " +
                    Optional
                            .ofNullable(parentChessboard.getFromParentToChildMove())
                            .map(Move::getTo)
                            .map(Field::getPieceType)
                            .map(PieceType::getColor)
                            .orElse(null) +
                    " " +
                    Optional
                            .ofNullable(parentChessboard.getFromParentToChildMove())
                            .map(Move::getTo)
                            .map(Field::getPieceType)
                            .map(PieceType::getName)
                            .orElse(null) +
                    " " +
                    Optional.ofNullable(parentChessboard.getFromParentToChildMove()).map(Move::getTo).map(Field::getCode).orElse(null));

            List<Chessboard> chessboardChildren = ChessboardDirector.createChessboardChildren(parentChessboard, activePlayerColorLevel);
            System.out.println("number of children= " + chessboardChildren.size());

            returnValue = chessboardChildren
                    .stream()
                    .peek(child -> System.out.println("childChessboard   = " + child))
                    .mapToInt(chessboard -> calculate(typeOfCalculation, new ChessboardValueParameter(chessboard, activePlayerColorLevel)))
                    .max()
                    .orElse(Integer.MIN_VALUE);

            System.out.println("returnValue       = " + returnValue);
            System.out.println("");
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
