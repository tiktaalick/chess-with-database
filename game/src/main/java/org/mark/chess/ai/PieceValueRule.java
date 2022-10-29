package org.mark.chess.ai;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Chessboard;
import org.mark.chess.player.PlayerColor;
import org.mark.chess.rulesengine.Rule;

public class PieceValueRule implements Rule<ChessboardValueParameter, ChessboardValue> {

    private final ChessboardValue chessboardValue;

    public PieceValueRule(ChessboardValue chessboardValue) {
        this.chessboardValue = chessboardValue;
    }

    @Override
    public ChessboardValue create() {
        return chessboardValue;
    }

    @Override
    public boolean isApplicable(ChessboardValueParameter chessboardValueParameter) {
        chessboardValue.setPieceValue(calculateGridValue(chessboardValueParameter.getChessboard(), chessboardValueParameter.getCurrentPlayerColor()));

        return false;
    }

    /**
     * Calculates the value of the current positions of the chess pieces on the chessboard for the active player.
     *
     * @param chessboard The backend representation of the chessboard.
     * @param currentPlayerColor The current player color.
     * @return The value of the current positions of the chess pieces on the chessboard.
     */
    private static int calculateGridValue(@NotNull Chessboard chessboard, PlayerColor currentPlayerColor) {
        return chessboard
                .getFields()
                .stream()
                .filter(field -> field.getPieceType() != null)
                .mapToInt(field -> field.getPieceType().getColor() == currentPlayerColor
                        ? field.getPieceType().getValue()
                        : -field.getPieceType().getValue())
                .sum();
    }
}
