package org.mark.chess.application;

import org.mark.chess.swing.FrontendChessboard;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.mark.chess.player.PlayerColor.WHITE;

/**
 * Main application class.
 */
@SpringBootApplication
public class Application {

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$-7s %2$-70s %5$s %n");
    }

    /**
     * Starts the application.
     *
     * @param args Ignored parameter.
     */
    public static void main(String[] args) {
        new FrontendChessboard(WHITE);
    }
}
