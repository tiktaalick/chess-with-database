package org.mark.chess;

import org.mark.chess.game.GameService;
import org.mark.chess.player.PlayerColor;
import org.mark.chess.swing.Board;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.mark.chess.player.PlayerColor.WHITE;

/**
 * Main application class.
 */
@SpringBootApplication
public class Application {

    private final GameService gameService;

    /**
     * Constructor for the main application class.
     *
     * @param gameService A service class for the front-end.
     */
    public Application(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Main method for this main application class. Creates the application.
     *
     * @param args Ignored parameter.
     */
    public static void main(String[] args) {
        ApplicationRepository.getInstance().startApplication(WHITE);
    }

    /**
     * Starts the application.
     *
     * @param humanPlayerColor The piece-type color with which the human plays.
     * @return The just created chessboard.
     */
    public Board startApplication(PlayerColor humanPlayerColor) {
        return Board.createBoard(gameService, humanPlayerColor);
    }
}
