package org.mark.chess.ai;

import org.mark.chess.board.Chessboard;
import org.mark.chess.player.PlayerColor;

import java.util.function.Function;

public class TotalValueOfAllPieces implements Function<ChessboardValueParameter, Integer> {

    /**
     * Calculates the total value of all the chess pieces on the chessboard for the active player.
     *
     * @param chessboardValueParameter A parameter that contains a {@link Chessboard} and a {@link PlayerColor}.
     * @return The value
     */
    @Override
    public Integer apply(ChessboardValueParameter chessboardValueParameter) {
        return chessboardValueParameter
                .getChessboard()
                .getFields()
                .stream()
                .filter(field -> field.getPieceType() != null)
                .mapToInt(field -> field.getPieceType().getColor() == chessboardValueParameter.getActivePlayerColor()
                        ? field.getPieceType().getValue()
                        : -field.getPieceType().getValue())
                .sum();
    }
}
