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
    private final CheckLogic checkLogic;
    private final MoveLogic  moveLogic;

    public GameLogic(CheckLogic checkLogic, MoveLogic moveLogic) {
        this.checkLogic = checkLogic;
        this.moveLogic = moveLogic;
    }

    public Game initializeGame(Board board, Color humanPlayerColor) {
        var game = new Game()
                .setInProgress(true)
                .setPlayers(Arrays.asList(new Human().setColor(Color.WHITE), new Human().setColor(Color.BLACK)))
                .setHumanPlayerColor(humanPlayerColor)
                .setCurrentPlayerColor(Color.WHITE);

        return game.setGrid(game.initializeGrid(game, board, checkLogic, moveLogic));
    }

    void setGameProgress(Game game, Field kingField) {
        game.setInProgress(game.isInProgress()
                ? (!kingField.isCheckMate() && !kingField.isStaleMate())
                : game.isInProgress());
    }
}
