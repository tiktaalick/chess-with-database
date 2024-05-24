package org.mark.chess.game;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.player.PlayerColor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A service class for the front-end.
 */
@Service
public class GameService {

    public static final  Level             DEFAULT_LOGLEVEL   = Level.INFO;
    private static final long              FROM_NANO_TO_MILLI = 1_000_000;
    private static final Logger            LOGGER             = Logger.getLogger(GameService.class.getName());
    protected static     Map<String, Long> durationMap        = new HashMap<>();

    public static void logDuration() {
        durationMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> LOGGER.info(() -> entry.getKey() + " took: " + (entry.getValue() / FROM_NANO_TO_MILLI + " milliseconds")));
    }

    public static void logDurationAndReset() {
        setLogLevel(Level.INFO);
        logDuration();
        setLogLevel(DEFAULT_LOGLEVEL);
        durationMap = new HashMap<>();
    }

    public static void setDefaultLoglevel() {
        setLogLevel(DEFAULT_LOGLEVEL);
    }

    public static void setLogLevel(Level targetLevel) {
        var root = Logger.getLogger("");
        root.setLevel(targetLevel);
        for (Handler handler : root.getHandlers()) {
            handler.setLevel(targetLevel);
        }
    }

    /**
     * Creates a new game.
     *
     * @param humanPlayerColor The piece-type color with which the human plays.
     * @return The created game.
     */
    public Game createGame(PlayerColor humanPlayerColor) {
        return Game.create(humanPlayerColor);
    }

    /**
     * Handles the left and right mouse clicks on the chessboard fields.
     *
     * @param game           The game.
     * @param leftRightClick An integer that indicates whether the event is a left, middle or right mouse click.
     * @param buttonId       The front-end chessboard field that was clicked.
     * @return The continued or restarted game.
     */
    public Game handleButtonClick(@NotNull Game game, int leftRightClick, int buttonId) {
        if (!game.isInProgress()) {
            return Game.restart(game);
        } else {
            var returnGame = game.handleButtonClick(leftRightClick, buttonId);
            logDurationAndReset();
            return returnGame;
        }
    }

    /**
     * Resets the valid to-moves for a specific chess piece on the chessboard to all the valid from-fields for the active player.
     *
     * @param game The game.
     */
    public void resetValidMoves(@NotNull Game game) {
        game.getChessboard().setValidFromFields(game.getMove(), game.getActivePlayer().getColor());
        logDurationAndReset();
    }

    public void storeDuration(String methodName, long nanoBefore, long nanoAfter) {
        durationMap.put(methodName, Optional.ofNullable(GameService.durationMap.get(methodName)).orElse(0L) + (nanoAfter - nanoBefore));
    }
}
