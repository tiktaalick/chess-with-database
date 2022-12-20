package org.mark.chess.board;

public class ChessboardDirector {

    private static ChessboardBuilder chessboardBuilder = new ChessboardBuilder();

    public Chessboard create() {
        return chessboardBuilder.create().build(); //.resetValidMoves()
    }
}
