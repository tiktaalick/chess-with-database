package org.mark.chess.move;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.king.isvalidmove.KingIsValidCastlingRule;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mark.chess.piece.general.PieceType.KING;

/**
 * Builds moves.
 */
public class MoveBuilder {

    private static final Logger        LOGGER          = Logger.getLogger(MoveBuilder.class.getName());
    private static final AiMoveBuilder aiMoveBuilder   = new AiMoveBuilder();
    private static final MoveBuilder   rookMoveBuilder = new MoveBuilder();
    protected            Move          move;

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
                .build(); //.performAiMove(game)
    }

    protected Move build() {
        LOGGER.log(Level.INFO, "MoveBuilder.build(): {0}", this.move);

        return this.move;
    }

    protected MoveBuilder changeTurn(@NotNull Game game) {
        game.changeTurn();

        LOGGER.info("MoveBuilder.changeTurn() ");

        return this;
    }

    protected MoveBuilder enableValidMoves(@NotNull Game game) {
        game.getChessboard().setValidToFields(this.move);

        LOGGER.log(Level.INFO, "MoveBuilder.enableValidMoves(): {0}", this.move);

        return this;
    }

    protected MoveBuilder moveRookIfCastling(Game game) {
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
                                                   : KingIsValidCastlingRule.ROOK_CASTLING_FROM_THE_RIGHT),
                    this.move.getFrom().getPieceType().getColor().getBaseline());

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

    protected MoveBuilder resetFrom() {
        move.getFrom().setPieceType(null);

        LOGGER.log(Level.INFO, "MoveBuilder.resetFrom(): {0}", this.move);

        return this;
    }

    protected MoveBuilder setKingFieldColors(@NotNull Game game) {
        if (game.isInProgress()) {
            game.getChessboard().setValidFromFields(this.move, game.getActivePlayer().getColor());
            game.getChessboard().setKingFieldColors(game);
        }

        LOGGER.log(Level.INFO, "MoveBuilder.setKingFieldColors(): {0}", this.move);

        return this;
    }

    protected MoveBuilder setMove(Move move) {
        this.move = move;

        LOGGER.log(Level.INFO, "MoveBuilder.setMove(): {0}", this.move);

        return this;
    }

    protected MoveBuilder setPieceTypeSpecificAttributes(Game game) {
        if (this.move.isValid()) {
            this.move.getFrom().getPieceType().setPieceTypeSpecificAttributes(game, this.move.getFrom(), this.move.getTo());
        }

        LOGGER.log(Level.INFO, "MoveBuilder.setPieceTypeSpecificAttributes(): {0}", this.move);

        return this;
    }

    private MoveBuilder performAiMove(Game game) {
        aiMoveBuilder.performAiMove(game);

        LOGGER.log(Level.INFO, "MoveBuilder.performAiMove(): {0}", this.move);

        return this;
    }

    private void performRookMove(Chessboard chessboard, Field from, Field to) {
        this.setMove(new Move(from)).setTo(chessboard, to).resetFrom().build();
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
