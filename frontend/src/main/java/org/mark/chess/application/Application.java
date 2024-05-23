package org.mark.chess.application;

import org.mark.chess.swing.FrontendChessboard;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mark.chess.player.PlayerColor.WHITE;

/**
 * Main application class.
 */
@SpringBootApplication
public class Application {

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT.%1$tL %4$-7s %2$-80s %5$s %n");
    }

    /**
     * Starts the application.
     *
     * @param args Ignored parameter.
     */
    public static void main(String[] args) {
        setLogLevel(Level.INFO);
        new FrontendChessboard(WHITE);
    }

    private static void setLogLevel(Level targetLevel) {
        Logger root = Logger.getLogger("");
        root.setLevel(targetLevel);
        for (Handler handler : root.getHandlers()) {
            handler.setLevel(targetLevel);
        }
    }
}
