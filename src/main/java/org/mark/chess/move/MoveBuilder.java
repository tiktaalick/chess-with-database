package org.mark.chess.move;

import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.game.Game;
import org.mark.chess.piece.isvalidmove.KingIsValidCastlingRule;

import static org.mark.chess.piece.PieceType.KING;

/**
 * Builds moves.
 */
public class MoveBuilder {

    private MoveDirector rookMoveDirector = new MoveDirector();

    private Move move;

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
    public MoveBuilder changeTurn(Game game) {
        game.changeTurn();

        return this;
    }

    /**
     * Marks the valid from-move and all the valid to-moves as valid and gives them nice, bright colors.
     *
     * @param game  The game.
     * @param field The field for which the valid moves will be searched.
     * @return The builder.
     */
    public MoveBuilder enableValidMoves(Game game, Field field) {
        game.enableValidMoves(field);

        return this;
    }

    /**
     * Moves the rook if the main move was a king performing a castling move.
     *
     * @param game The game.
     * @return The builder.
     */
    public MoveBuilder moveRookIfCastling(Game game) {
        if (this.move.getFrom().getPieceType().getName().equals(KING) &&
                KingIsValidCastlingRule.isValidCastling(game.getGrid(),
                        this.move.getFrom(),
                        this.move.getTo(),
                        this.move.getTo().getCoordinates().getX(),
                        false,
                        true)) {

            var rookCoordinates = new Coordinates((this.move.getTo().getCoordinates().getX() == KingIsValidCastlingRule.KING_CASTLING_TO_THE_LEFT
                    ? KingIsValidCastlingRule.ROOK_CASTLING_FROM_THE_LEFT
                    : KingIsValidCastlingRule.ROOK_CASTLING_FROM_THE_RIGHT), this.move.getFrom().getPieceType().getColor().getBaseline());

            var rookFromField = game.getGrid().getField(rookCoordinates);
            var rookToField = game
                    .getGrid()
                    .getField(rookCoordinates.setX(this.move.getTo().getCoordinates().getX() == KingIsValidCastlingRule.KING_CASTLING_TO_THE_LEFT
                            ? KingIsValidCastlingRule.ROOK_CASTLING_TO_THE_RIGHT
                            : KingIsValidCastlingRule.ROOK_CASTLING_TO_THE_LEFT));

            rookMoveDirector.performRookMove(game.getGrid(), rookFromField, rookToField);
        }

        return this;
    }

    /**
     * Clears the from-field.
     *
     * @return The builder.
     */
    public MoveBuilder resetFrom() {
        move.getFrom().resetField();

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
    public MoveBuilder setKingFieldColors(Game game) {
        game.setKingFieldColors(game.resetValidMoves());

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
        this.move.getFrom().getPieceType().setPieceTypeSpecificAttributes(game, this.move.getFrom(), this.move.getTo());

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
     * @param grid  The back-end representation of a chessboard.
     * @param field The field.
     * @return The builder.
     */
    public MoveBuilder setTo(Grid grid, Field field) {
        this.move = this.move.setTo(grid, field);

        return this;
    }
}
