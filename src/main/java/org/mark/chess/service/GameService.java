package org.mark.chess.service;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Game;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    public Game createGame(Board board, Color humanPlayerColor) {
        return Game.create(board, humanPlayerColor);
    }

    public void handleButtonClick(Game game, Board board, int buttonClick, Button button) {
        game.handleButtonClick(board, buttonClick, button);
    }

    public void resetValidMoves(Game game) {
        game.resetValidMoves();
    }
}
