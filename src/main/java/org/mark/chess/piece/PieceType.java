package org.mark.chess.piece;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.player.PlayerColor;

/**
 * Superclass for all the chess pieces.
 */
@Data
@Accessors(chain = true)
public abstract class PieceType {

    public static final String BISHOP = "bishop";
    public static final String KING   = "king";
    public static final String KNIGHT = "knight";
    public static final String PAWN   = "pawn";
    public static final String QUEEN  = "queen";
    public static final String ROOK   = "rook";

    private PlayerColor color;
    private boolean     kickedOff;
    private boolean     isPawnBeingPromoted;

    /**
     * Constructor
     *
     * @param color The color of the piece.
     */
    protected PieceType(PlayerColor color) {
        this.color = color;
    }

    public abstract String getName();

    public abstract PieceType getNextPawnPromotion();

    public abstract int getValue();

    public abstract boolean isValidMove(IsValidMoveParameter isValidMoveParameter);

    public abstract void setPieceTypeSpecificFields(Game game, Field from, Field to);
}
