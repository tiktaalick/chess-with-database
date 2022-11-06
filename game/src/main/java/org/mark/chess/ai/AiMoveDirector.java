package org.mark.chess.ai;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.game.Game;
import org.mark.chess.move.Move;
import org.mark.chess.move.MoveDirector;

import static org.mark.chess.player.PlayerType.HUMAN;

/**
 * Contains all possible computer moves, which adds up to the staggering number of one.
 */
public class AiMoveDirector extends MoveDirector {

    /**
     * Performs a computer move if applicable.
     *
     * @param game The game.
     * @return The built move.
     */
    public Move performAiMove(@NotNull Game game) {
        return game.getActivePlayer().getPlayerType() == HUMAN
                ? generalMoveBuilder.build()
                : generalMoveBuilder
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
