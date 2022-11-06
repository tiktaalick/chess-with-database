package org.mark.chess.ai;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Chessboard;
import org.mark.chess.player.PlayerColor;
import org.mark.chess.rulesengine.Rule;

/**
 * A rule that calculates the value of a board position based upon the piece values.
 */
public class PieceValueRule implements Rule<ChessboardValueParameter, ChessboardValue> {

    private final ChessboardValue chessboardValue;

    /**
     * Constructor.
     *
     * @param chessboardValue The chessboard value.
     */
    public PieceValueRule(ChessboardValue chessboardValue) {
        this.chessboardValue = chessboardValue;
    }

    @Override
    public ChessboardValue createResult() {
        return chessboardValue;
    }

    @Override
    public boolean hasResult(ChessboardValueParameter chessboardValueParameter) {
        chessboardValue.setPieceValue(calculateGridValue(chessboardValueParameter.getChessboard(), chessboardValueParameter.getActivePlayerColor()));

        return false;
    }

    /**
     * Calculates the value of the current positions of the chess pieces on the chessboard for the active player.
     *
     * @param chessboard        The backend representation of the chessboard.
     * @param activePlayerColor The active player color.
     * @return The value of the current positions of the chess pieces on the chessboard.
     */
    private static int calculateGridValue(@NotNull Chessboard chessboard, PlayerColor activePlayerColor) {
        return chessboard
                .getFields()
                .stream()
                .filter(field -> field.getPieceType() != null)
                .mapToInt(field -> field.getPieceType().getColor() == activePlayerColor
                        ? field.getPieceType().getValue()
                        : -field.getPieceType().getValue())
                .sum();
    }
}
