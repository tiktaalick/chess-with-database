package org.mark.chess.board;

import org.mark.chess.player.PlayerColor;

import java.util.List;

public class ChessboardDirector {

    private static ChessboardBuilder chessboardBuilder = new ChessboardBuilder();

    public static List<Chessboard> createChessboardChildren(Chessboard chessboard, PlayerColor activePlayerColor) {
        return chessboardBuilder.setChessboard(chessboard).resetValidMoves(activePlayerColor).createChildren().buildChildren();
    }

    public Chessboard createChessboard() {
        return chessboardBuilder.createChessboard().build();
    }
}
