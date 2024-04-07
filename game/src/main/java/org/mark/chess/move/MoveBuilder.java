package org.mark.chess.move;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.ai.AiMoveDirector;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.isvalidmove.KingIsValidCastlingRule;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mark.chess.piece.PieceType.KING;

/**
 * Builds moves.
 */
public class MoveBuilder {

    private static final Logger         LOGGER          = Logger.getLogger(MoveBuilder.class.getName());
    private static final AiMoveDirector aiMoveDirector  = new AiMoveDirector();
    private static final MoveBuilder    rookMoveBuilder = new MoveBuilder();
    protected            Move           move;

    /**
     * Returns the built move.
     *
     * @return The built move.
     */
    public Move build() {
        LOGGER.log(Level.INFO, "MoveBuilder.build(): {0}", this.move);

        return this.move;
    }

    /**
     * Changes the active player.
     *
     * @param game The game.
     * @return The builder.
     */
    public MoveBuilder changeTurn(@NotNull Game game) {
        game.changeTurn();

        LOGGER.info("MoveBuilder.changeTurn() ");

        return this;
    }

    /**
     * Marks the valid from-move and all the valid to-moves as valid and gives them nice, bright colors.
     *
     * @param game The game.
     * @return The builder.
     */
    public MoveBuilder enableValidMoves(@NotNull Game game) {
        game.getChessboard().enableValidMoves(this.move.getFrom(), game.getActivePlayer().getColor());

        LOGGER.log(Level.INFO, "MoveBuilder.enableValidMoves(): {0}", this.move);

        return this;
    }

    /**
     * Moves the rook if the main move was a king performing a castling move.
     *
     * @param game The game.
     * @return The builder.
     */
    public MoveBuilder moveRookIfCastling(Game game) {
        if (this.move.isValid() &&
                this.move.getFrom().getPieceType().getName().equals(KING) &&
                KingIsValidCastlingRule.isValidCastling(game.getChessboard(),
                        this.move.getFrom(),
                        this.move.getTo(),
                        this.move.getTo().getCoordinates().getX(),
                        false,
                        true)) {

            var rookCoordinates = new Coordinates((this.move.getTo().getCoordinates().getX() == KingIsValidCastlingRule.KING_CASTLING_TO_THE_LEFT
                    ? KingIsValidCastlingRule.ROOK_CASTLING_FROM_THE_LEFT
                    : KingIsValidCastlingRule.ROOK_CASTLING_FROM_THE_RIGHT), this.move.getFrom().getPieceType().getColor().getBaseline());

            var rookFromField = game.getChessboard().getField(rookCoordinates);
            var rookToField = game
                    .getChessboard()
                    .getField(rookCoordinates.setX(this.move.getTo().getCoordinates().getX() == KingIsValidCastlingRule.KING_CASTLING_TO_THE_LEFT
                            ? KingIsValidCastlingRule.ROOK_CASTLING_TO_THE_RIGHT
                            : KingIsValidCastlingRule.ROOK_CASTLING_TO_THE_LEFT));

            rookMoveBuilder.performRookMove(game.getChessboard(), rookFromField, rookToField);
        }

        LOGGER.log(Level.INFO, "MoveBuilder.moveRookIfCastling(): {0}", this.move);

        return this;
    }

    /**
     * Performs the from-part of a move.
     *
     * @param game       The game.
     * @param move       The move.
     * @param fieldClick The from-field that has been clicked upon.
     * @return The built move.
     */
    public Move performFromMove(Game game, Move move, Field fieldClick) {
        return this.setMove(move).setFrom(fieldClick).enableValidMoves(game).build();
    }

    /**
     * Performs the resetting of a move.
     *
     * @param game The game.
     * @param move The move.
     * @return The built move.
     */
    public Move performResetMove(Game game, Move move) {
        return this.setMove(move).setKingFieldColors(game).build();
    }

    /**
     * Performs the to-part of a move.
     *
     * @param game       The game.
     * @param move       The move.
     * @param fieldClick The to-field that has been clicked upon.
     * @return The built move.
     */
    public Move performToMove(@NotNull Game game, Move move, Field fieldClick) {
        return this
                .setMove(move)
                .setTo(game.getChessboard(), fieldClick)
                .setPieceTypeSpecificAttributes(game)
                .moveRookIfCastling(game)
                .changeTurn(game)
                .resetFrom()
                .setKingFieldColors(game)
                .performAiMove(game)
                .build();
    }

    /**
     * Clears the from-field.
     *
     * @return The builder.
     */
    public MoveBuilder resetFrom() {
        move.getFrom().setPieceType(null);

        LOGGER.log(Level.INFO, "MoveBuilder.resetFrom(): {0}", this.move);

        return this;
    }

    /**
     * Colors the field of a king that is in checkmate or stalemate and then marks the game as finished.
     *
     * @param game The game.
     * @return The builder.
     */
    public MoveBuilder setKingFieldColors(@NotNull Game game) {
        if (game.isInProgress()) {
            List<Field> allValidMoves = game.getChessboard().resetValidMoves(this.move, game.getActivePlayer().getColor());
            game.getChessboard().setKingFieldColors(game, allValidMoves);
        }

        LOGGER.log(Level.INFO, "MoveBuilder.setKingFieldColors(): {0}", this.move);

        return this;
    }

    /**
     * Sets a new move.
     *
     * @param move The move.
     * @return The builder.
     */
    public MoveBuilder setMove(Move move) {
        this.move = move;

        LOGGER.log(Level.INFO, "MoveBuilder.setMove(): {0}", this.move);

        return this;
    }

    /**
     * Sets piece-type specific attributes, like: is the pawn being promoted? or: has the king moved? which is relevant for castling.
     *
     * @param game The game.
     * @return The builder.
     */
    public MoveBuilder setPieceTypeSpecificAttributes(Game game) {
        if (this.move.isValid()) {
            this.move.getFrom().getPieceType().setPieceTypeSpecificAttributes(game, this.move.getFrom(), this.move.getTo());
        }

        LOGGER.log(Level.INFO, "MoveBuilder.setPieceTypeSpecificAttributes(): {0}", this.move);

        return this;
    }

    private MoveBuilder performAiMove(Game game) {
        aiMoveDirector.performAiMove(game);

        LOGGER.log(Level.INFO, "MoveBuilder.performAiMove(): {0}", this.move);

        return this;
    }

    private Move performRookMove(Chessboard chessboard, Field from, Field to) {
        return this.setMove(new Move(from)).setTo(chessboard, to).resetFrom().build();
    }

    private MoveBuilder setFrom(Field field) {
        this.move.setFrom(field);

        LOGGER.log(Level.INFO, "MoveBuilder.setFrom(): {0}", this.move);

        return this;
    }

    private MoveBuilder setTo(Chessboard chessboard, Field field) {
        this.move = this.move.setTo(chessboard, field);

        LOGGER.log(Level.INFO, "MoveBuilder.setTo(): {0}", this.move);

        return this;
    }
}
