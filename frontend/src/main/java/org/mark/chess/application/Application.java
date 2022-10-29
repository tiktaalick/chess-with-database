package org.mark.chess.application;

import org.mark.chess.game.GameService;
import org.mark.chess.player.PlayerColor;
import org.mark.chess.swing.FrontendChessboard;
import org.mark.chess.swing.FrontendChessboardBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.mark.chess.player.PlayerColor.WHITE;

/**
 * Main application class.
 */
@SpringBootApplication
public class Application {

    private static FrontendChessboardBuilder frontendChessboardBuilder = new FrontendChessboardBuilder();
    private static GameService               gameService               = new GameService();

    public static FrontendChessboardBuilder getBoardBuilder() {
        return frontendChessboardBuilder;
    }

    public static void setBoardBuilder(FrontendChessboardBuilder builder) {
        frontendChessboardBuilder = builder;
    }

    /**
     * Main method for this main application class. Creates the application.
     *
     * @param args Ignored parameter.
     */
    public static void main(String[] args) {
        ApplicationRepository.getInstance().startApplication(WHITE);
    }

    public static void setGameService(GameService gameService) {
        Application.gameService = gameService;
    }

    /**
     * Starts the application.
     *
     * @param humanPlayerColor The piece-type color with which the human plays.
     * @return The just created chessboard.
     */
    public FrontendChessboard startApplication(PlayerColor humanPlayerColor) {
        return frontendChessboardBuilder.setBoard(gameService, humanPlayerColor).createButtons().initialize().updateButtons().build();
    }
}
