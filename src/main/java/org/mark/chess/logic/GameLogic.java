package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Human;
import org.mark.chess.swing.Board;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class GameLogic {
    private final GridLogic gridLogic;

    public GameLogic(GridLogic gridLogic) {
        this.gridLogic = gridLogic;
    }

    public Game initializeGame(Board board, Color humanPlayerColor) {
        var game = new Game()
                .setInProgress(true)
                .setPlayers(Arrays.asList(new Human().setColor(Color.WHITE), new Human().setColor(Color.BLACK)))
                .setHumanPlayerColor(humanPlayerColor)
                .setCurrentPlayerColor(Color.WHITE);

        return game.setGrid(gridLogic.initializeGrid(game, board));
    }

    void setGameProgress(Game game, Field kingField) {
        game.setInProgress(game.isInProgress()
                ? (!kingField.isCheckMate() && !kingField.isStaleMate())
                : game.isInProgress());
    }
}
