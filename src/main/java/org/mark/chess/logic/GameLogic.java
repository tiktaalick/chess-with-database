package org.mark.chess.logic;

import org.mark.chess.enums.GameStatus;
import org.mark.chess.model.Game;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

public class GameLogic {
    @Autowired
    private GridLogic gridLogic;

    public Game initializeGame(Board board) {
        return new Game()
                .grid(gridLogic.initializeGrid(board))
                .gameStatus(GameStatus.IN_PROGRESS);
    }

    public void endGame(Game game, boolean hasWon) {
        if (hasWon) {
            game.setWon();
        } else {
            game.setLost();
        }
    }
}
