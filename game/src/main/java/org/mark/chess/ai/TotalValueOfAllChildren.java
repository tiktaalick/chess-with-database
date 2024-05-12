package org.mark.chess.ai;

import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Field;
import org.mark.chess.move.Move;
import org.mark.chess.player.PlayerColor;

import java.util.function.Function;

public class TotalValueOfAllChildren implements Function<ChessboardValueParameter, Integer> {

    /**
     * Calculates the total value of all the chess pieces on the chessboard for the active player.
     *
     * @param chessboardValueParameter A parameter that contains a {@link Chessboard} and a {@link PlayerColor}.
     * @return The value
     */
    @Override
    public Integer apply(ChessboardValueParameter chessboardValueParameter) {
        var chessboard = chessboardValueParameter.getChessboard();
        var activePlayerColor = chessboardValueParameter.getActivePlayerColor();

        chessboard.setValidFromFields(new Move(new Field(null)), chessboardValueParameter.getActivePlayerColor());
        var totalValue = chessboard.getChildren().stream().mapToInt(child -> calculateTotalValue(child, activePlayerColor.getOpposite())).sum();

        return totalValue;
    }

    private static int calculateTotalValue(Chessboard chessboard, PlayerColor activePlayerColor) {
        return chessboard
                .getFields()
                .stream()
                .filter(field -> field.getPieceType() != null)
                .mapToInt(field -> field.getPieceType().getColor() ==
                                           activePlayerColor
                                   ? field
                                           .getPieceType()
                                           .getValue()
                                   : -field.getPieceType().getValue())
                .sum();
    }
}
