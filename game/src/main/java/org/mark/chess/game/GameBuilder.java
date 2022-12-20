package org.mark.chess.game;

import org.mark.chess.board.ChessboardDirector;
import org.mark.chess.player.PlayerColor;

public class GameBuilder {

    private static ChessboardDirector chessboardDirector = new ChessboardDirector();

    private Game game;

    public Game build() {
        return this.game;
    }

    public GameBuilder createGame(PlayerColor humanPlayerColor) {
        this.game = new Game(humanPlayerColor, chessboardDirector.create());

        return this;
    }

    public GameBuilder resetValidMoves() {
        this.game.resetValidMoves(); // Move to Chessboard
        return this;
    }
}
