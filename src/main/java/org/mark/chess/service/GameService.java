package org.mark.chess.service;

import org.mark.chess.enums.Color;
import org.mark.chess.logic.BoardLogic;
import org.mark.chess.logic.GameLogic;
import org.mark.chess.model.Game;
import org.mark.chess.model.Move;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private BoardLogic boardlogic;
    private GameLogic  gameLogic;

    @Autowired
    public GameService(BoardLogic boardlogic, GameLogic gameLogic) {
        this.boardlogic = boardlogic;
        this.gameLogic = gameLogic;
    }

    public void handleButtonClick(Game game, Board board, Move move, int buttonClick, Button button) {
        boardlogic.handleButtonClick(game, board, move, buttonClick, button);
    }

    public void initializeBoard(Game game, Board board, Move move) {
        boardlogic.initializeBoard(game, board, move);
    }

    public Game initializeGame(Board board, Color humanPlayerColor) {
        return gameLogic.initializeGame(board, humanPlayerColor);
    }
}
