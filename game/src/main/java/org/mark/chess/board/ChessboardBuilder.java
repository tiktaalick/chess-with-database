package org.mark.chess.board;

public class ChessboardBuilder {

    private Chessboard chessboard;

    public Chessboard build() {
        return chessboard;
    }

    public ChessboardBuilder create() {
        this.chessboard = Chessboard.create();

        return this;
    }

//    public MoveBuilder resetValidMoves() {
//
//    }
}
