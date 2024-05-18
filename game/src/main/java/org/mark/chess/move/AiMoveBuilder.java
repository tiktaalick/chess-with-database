package org.mark.chess.move;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;

import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mark.chess.player.PlayerType.HUMAN;

public class AiMoveBuilder extends MoveBuilder {

    private static final Logger LOGGER = Logger.getLogger(AiMoveBuilder.class.getName());

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

    /**
     * Performs a computer move if applicable.
     *
     * @param game The game.
     * @return The built move.
     */
    public Move performAiMove(@NotNull Game game) {
        return game.getActivePlayer().getPlayerType() == HUMAN
               ? this.build()
               : this
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

    private AiMoveBuilder createAiFrom(Game game) {
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

    private AiMoveBuilder createAiTo(Game game) {
        if (game.isInProgress()) {
            var toField = game.getChessboard().getChildren().getLast().getFromParentToChildMove().getTo();

            this.move = this.move.setTo(game.getChessboard(), toField);
        }

        LOGGER.log(Level.INFO, "AiMoveBuilder.createAiTo(): {0}", this.move);

        return this;
    }
}
