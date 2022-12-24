package org.mark.chess.game;

import org.mark.chess.player.PlayerColor;

public class GameDirector {

    private static GameBuilder gameBuilder = new GameBuilder();

    public Game createGame(PlayerColor humanPlayerColor) {
        return gameBuilder.createGame(humanPlayerColor).build();
    }

    public Game restartGame(PlayerColor humanPlayerColor) {
        return gameBuilder.createGame(humanPlayerColor).resetValidMoves().build();
    }
}
