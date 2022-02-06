package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.enums.GameStatus;
import org.mark.chess.model.Game;
import org.mark.chess.model.Human;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class GameLogic {
    @Autowired
    private GridLogic gridLogic;

    public Game initializeGame(Board board) {
        Game game = new Game()
                .setGameStatus(GameStatus.IN_PROGRESS)
                .setPlayers(Arrays.asList(new Human().setColor(Color.WHITE), new Human().setColor(Color.BLACK)))
                .setCurrentPlayerId(Color.WHITE.ordinal());

        return game.setGrid(gridLogic.initializeGrid(game, board));
    }

    public void endGame(Game game, boolean hasWon) {
        if (hasWon) {
            game.setWon();
        } else {
            game.setLost();
        }
    }
}
