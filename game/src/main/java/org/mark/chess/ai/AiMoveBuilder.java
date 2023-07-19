package org.mark.chess.ai;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.move.Move;
import org.mark.chess.move.MoveBuilder;

import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AiMoveBuilder extends MoveBuilder {

    private static final Logger LOGGER = Logger.getLogger(AiMoveBuilder.class.getName());

    /**
     * Sets the from-part of the AI move.
     *
     * @param game The game.
     * @return The builder.
     */
    public AiMoveBuilder createAiFrom(Game game) {
        setMove(new Move(game
                .getChessboard()
                .getFields()
                .stream()
                .filter(field -> field.getValue() != null)
                .filter(field -> field.getPieceType() != null)
                .filter(field -> field.getPieceType().getColor() == game.getActivePlayer().getColor())
                .max(Comparator.comparing(Field::getValue))
                .orElse(new Field(null))));

        LOGGER.log(Level.INFO, "AiMoveBuilder.createAiFrom(): {0}", this.move);

        return this;
    }

    /**
     * Sets the to-part of the AI move.
     *
     * @param game The game.
     * @return The builder.
     */
    public AiMoveBuilder createAiTo(Game game) {
        game.getChessboard().setKingFieldColors(game, game.getChessboard().getAllValidFromToCombinations().get(move.getFrom()));

        if (game.isInProgress()) {
            var toField = game
                    .getChessboard()
                    .getAllValidFromToCombinations()
                    .get(move.getFrom())
                    .stream()
                    .max(Comparator.comparing(Field::getValue))
                    .orElse(new Field(null));

            this.move = this.move.setTo(game.getChessboard(), toField);
        }

        LOGGER.log(Level.INFO, "AiMoveBuilder.createAiTo(): {0}", this.move);

        return this;
    }

    /**
     * Marks the valid from-move and all the valid to-moves as valid and gives them nice, bright colors.
     *
     * @param game The game.
     * @return The builder.
     */
    @Override
    public AiMoveBuilder enableValidMoves(@NotNull Game game) {
        LOGGER.log(Level.INFO, "AiMoveBuilder.enableValidMoves(): {0}", this.move);

        return (AiMoveBuilder) super.enableValidMoves(game);
    }
}
