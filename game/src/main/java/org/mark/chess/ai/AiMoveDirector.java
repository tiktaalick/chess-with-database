package org.mark.chess.ai;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.game.Game;
import org.mark.chess.move.Move;
import org.mark.chess.move.MoveDirector;

import java.util.logging.Logger;

import static org.mark.chess.player.PlayerType.HUMAN;

/**
 * Contains all possible computer moves, which adds up to the staggering number of one.
 */
public class AiMoveDirector extends MoveDirector {

    private static final Logger LOGGER = Logger.getLogger(AiMoveDirector.class.getName());

    protected static AiMoveBuilder aiMoveBuilder = new AiMoveBuilder();

    public static void setAiMoveBuilder(AiMoveBuilder newBuilder) {
        aiMoveBuilder = newBuilder;
    }

    /**
     * Performs a computer move if applicable.
     *
     * @param game The game.
     * @return The built move.
     */
    public Move performAiMove(@NotNull Game game) {
        return game.getActivePlayer().getPlayerType() == HUMAN
                ? aiMoveBuilder.build()
                : aiMoveBuilder
                        .createAiFrom(game)
                        .enableValidMoves(game)
                        .createAiTo(game)
                        .setPieceTypeSpecificAttributes(game)
                        .moveRookIfCastling(game)
                        .changeTurn(game)
                        .resetFrom()
                        .setKingFieldColors(game)
                        .build();
    }
}
