package org.mark.chess.game;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.player.PlayerColor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * A service class for the front-end.
 */
@Service
public class GameService {

    private static final long   FROM_NANO_TO_MILLI = 1000_000;
    private static final Logger LOGGER             = Logger.getLogger(GameService.class.getName());

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
            return game.handleButtonClick(leftRightClick, buttonId);
        }
    }

    public void logDuration(Map<String, Long> durationMap) {
        durationMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> LOGGER.info(() -> entry.getKey() + " took: " + (entry.getValue() / FROM_NANO_TO_MILLI + " milliseconds")));
    }

    public void resetDuration(Map<String, Long> durationMap) {
        durationMap = new HashMap<>();
    }

    /**
     * Resets the valid to-moves for a specific chess piece on the chessboard to all the valid from-fields for the active player.
     *
     * @param game The game.
     */
    public void resetValidMoves(@NotNull Game game) {
        game.getChessboard().setValidFromFields(game.getMove(), game.getActivePlayer().getColor());
    }

    public void storeDuration(Map<String, Long> durationMap, String methodName, long nanoBefore, long nanoAfter) {
        durationMap.put(methodName, Optional.ofNullable(durationMap.get(methodName)).orElse(0L) + (nanoAfter - nanoBefore));
    }
}
