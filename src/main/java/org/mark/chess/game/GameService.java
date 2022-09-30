package org.mark.chess.game;

import org.mark.chess.player.PlayerColor;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.springframework.stereotype.Service;

/**
 * A service class for the front-end.
 */
@Service
public class GameService {

    /**
     * Creates a new game.
     *
     * @param board            The front-end chessboard.
     * @param humanPlayerColor The piece-type color with which the human plays.
     * @return The created game.
     */
    public Game createGame(Board board, PlayerColor humanPlayerColor) {
        return Game.create(board, humanPlayerColor);
    }

    /**
     * Handles the left and right mouse clicks on the chessboard fields.
     *
     * @param game        The game.
     * @param board       The front-end chessboard.
     * @param buttonClick An integer that indicates whether the event is a left, middle or right mouse click.
     * @param button      The front-end chessboard field that was clicked.
     */
    public void handleButtonClick(Game game, Board board, int buttonClick, Button button) {
        game.handleButtonClick(board, buttonClick, button);
    }

    /**
     * Resets the valid to-moves for a specific chess piece on the chessboard to all the valid from-fields for the active player.
     *
     * @param game The game.
     */
    public void resetValidMoves(Game game) {
        game.resetValidMoves();
    }
}
