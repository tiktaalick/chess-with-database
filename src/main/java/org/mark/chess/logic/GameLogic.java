package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Game;
import org.mark.chess.model.Human;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class GameLogic {
    @Autowired
    private GridLogic gridLogic;

    public Game initializeGame(Board board, Color humanPlayerColor) {
        Game game = new Game()
                .setInProgress(true)
                .setPlayers(Arrays.asList(new Human().setColor(Color.WHITE), new Human().setColor(Color.BLACK)))
                .setHumanPlayerColor(humanPlayerColor)
                .setCurrentPlayerColor(Color.WHITE);

        return game.setGrid(gridLogic.initializeGrid(game, board));
    }
}
