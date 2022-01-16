package org.mark.chess.service;

import org.mark.chess.logic.BoardLogic;
import org.mark.chess.logic.GameLogic;
import org.mark.chess.model.Game;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;

public class GameService {
    @Autowired
    private BoardLogic boardlogic;

    @Autowired
    private GameLogic gameLogic;

    public void initializeBoard(Board board) {
        boardlogic.initializeBoard(board);
    }

    public Game initializeGame(Board board) {
        return gameLogic.initializeGame(board);
    }

    public void handleButtonClick(Game game, Board board, int buttonClick, JButton button) {
        boardlogic.handleButtonClick(game, board, buttonClick, button);
    }
}
