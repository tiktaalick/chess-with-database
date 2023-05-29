package org.mark.chess.move;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.ai.AiMoveDirector;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.isvalidmove.KingIsValidCastlingRule;

import java.util.Comparator;
import java.util.List;

import static org.mark.chess.piece.PieceType.KING;

/**
 * Builds moves.
 */
public class MoveBuilder {

    private final AiMoveDirector aiMoveDirector = new AiMoveDirector();

    protected Move         move;
    private   MoveDirector rookMoveDirector = new MoveDirector();

    /**
     * Returns the built move.
     *
     * @return The built move.
     */
    public Move build() {
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

        return this;
    }

    /**
     * Sets the from-part of the AI move.
     *
     * @param game The game.
     * @return The builder.
     */
    public MoveBuilder createAiFrom(Game game) {
        this.move.setFrom(game
                .getChessboard()
                .getFields()
                .stream()
                .filter(field -> field.getValue() != null)
                .filter(field -> field.getPieceType() != null)
                .filter(field -> field.getPieceType().getColor() == game.getActivePlayer().getColor())
                .max(Comparator.comparing(Field::getValue))
                .orElse(new Field(null)));

        return this;
    }

    /**
     * Sets the to-part of the AI move.
     *
     * @param game The game.
     * @return The builder.
     */
    public MoveBuilder createAiTo(Game game) {
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

            rookMoveDirector.performRookMove(game.getChessboard(), rookFromField, rookToField);
        }

        return this;
    }

    /**
     * Performs a move based on artificial intelligence.
     *
     * @param game The game.
     * @return The builder.
     */
    public MoveBuilder performAiMove(Game game) {
        aiMoveDirector.performAiMove(game);

        return this;
    }

    /**
     * Clears the from-field.
     *
     * @return The builder.
     */
    public MoveBuilder resetFrom() {
        move.getFrom().setPieceType(null);

        return this;
    }

    /**
     * Sets the field as the from-part of the move.
     *
     * @param field The field.
     * @return The builder.
     */
    public MoveBuilder setFrom(Field field) {
        this.move.setFrom(field);

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

        return this;
    }

    /**
     * Sets the MoveDirector for the rook move.
     *
     * @param rookMoveDirector The move director.
     * @return The move builder.
     */
    public MoveBuilder setRookMoveDirector(MoveDirector rookMoveDirector) {
        this.rookMoveDirector = rookMoveDirector;

        return this;
    }

    /**
     * Sets the field as the to-part of the move.
     *
     * @param chessboard The back-end representation of a chessboard.
     * @param field      The field.
     * @return The builder.
     */
    public MoveBuilder setTo(Chessboard chessboard, Field field) {
        this.move = this.move.setTo(chessboard, field);

        return this;
    }
}
