package org.mark.chess.piece.general;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.player.PlayerColor;

import java.util.List;

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
    private boolean     isBeingPromoted;

    /**
     * Constructor
     *
     * @param color The color of the piece.
     */
    protected PieceType(PlayerColor color) {
        this.color = color;
    }

    public abstract List<Coordinates> createCandidateToFieldCoordinates(Field from);

    public abstract String getName();

    public abstract PieceType getNextPawnPromotion();

    public abstract int getValue();

    public abstract boolean isValidMove(IsValidMoveParameter isValidMoveParameter);

    public abstract void setPieceTypeSpecificAttributes(Game game, Field from, Field to);
}
