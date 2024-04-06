package org.mark.chess.application;

import org.mark.chess.swing.FrontendChessboard;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.mark.chess.player.PlayerColor.WHITE;

/**
 * Main application class.
 */
@SpringBootApplication
public class Application {

    /**
     * Starts the application.
     *
     * @param args Ignored parameter.
     */
    public static void main(String[] args) {
        new FrontendChessboard(WHITE);
    }
}
