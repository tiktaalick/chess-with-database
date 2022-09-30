package org.mark.chess.move;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.game.Game;
import org.mark.chess.piece.PieceType;

import java.util.Arrays;

import static org.mark.chess.piece.PieceType.PAWN;

/**
 * Contains methods that are move related.
 */
@Data
@Accessors(chain = true)
public class Move {

    private PieceType pieceType;
    private Field     from;
    private Field     to;
    private Move      rookMove;

    /**
     * Constructor.
     *
     * @param from The field from which a chess piece will move.
     */
    public Move(Field from) {
        this.pieceType = from.getPieceType();
        this.from = from;
    }

    /**
     * Indicates whether a piece is in the process of moving.
     *
     * @param field The field from which or to which a piece might be moving.
     * @return True if during a move.
     */
    public boolean isDuringAMove(Field field) {
        return from != null && to != null && Arrays.asList(from.getCode(), to.getCode()).contains(field.getCode());
    }

    /**
     * Indicates whether the field is a from-field and not a to-field.
     *
     * @param game  The game.
     * @param field The field.
     * @return True if the field is a from-field.
     */
    public boolean isFrom(Game game, Field field) {
        return field.getPieceType() != null &&
                field.getPieceType().getColor() == game.getPlayers().get(game.getCurrentPlayerColor().ordinal()).getColor();
    }

    /**
     * Sets the field as the from-part of the move.
     *
     * @param from The field.
     * @return The move.
     */
    public Move setFrom(Field from) {
        this.pieceType = from.getPieceType();
        this.from = from;
        this.to = null;

        from.setValidFrom(true);

        return this;
    }

    /**
     * Sets the field as the to-part of the move. Captures en passant a pawn en passant if during an en passant move.
     *
     * @param grid The back-end representation of a chessboard.
     * @param to   The field.
     * @return The move.
     */
    public Move setTo(Grid grid, Field to) {
        if (isCaptureEnPassant(this, to)) {
            captureEnPassant(grid, from, to);
        }

        setTo(to.setPieceType(from.getPieceType()));
        to.getButton().setText(null);
        to.getButton().setIcon(from.getButton().getIcon());

        return this;
    }

    private static void captureEnPassant(Grid grid, Field from, Field to) {
        grid.getField(new Coordinates(to.getCoordinates().getX(), from.getCoordinates().getY())).resetField();
    }

    private static boolean isCaptureEnPassant(Move move, Field to) {
        return move.getFrom().getPieceType().getName().equals(PAWN) &&
                move.getFrom().getCoordinates().getX() != to.getCoordinates().getX() &&
                to.getPieceType() == null;
    }
}
