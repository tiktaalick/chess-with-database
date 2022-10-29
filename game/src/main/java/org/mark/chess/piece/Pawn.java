package org.mark.chess.piece;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Field;
import org.mark.chess.board.Chessboard;
import org.mark.chess.game.Game;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.PawnIsValidMoveRulesEngine;
import org.mark.chess.piece.maybecapturedenpassant.PawnMayBeCapturedEnPassantRulesEngine;
import org.mark.chess.player.PlayerColor;

/**
 * Contains pawn related methods.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Pawn extends PieceType {

    private static final int                        PIECE_VALUE                = 1;
    private static final PawnIsValidMoveRulesEngine pawnIsValidMoveRulesEngine = new PawnIsValidMoveRulesEngine();

    private PawnMayBeCapturedEnPassantRulesEngine pawnMayBeCapturedEnPassantRulesEngine = new PawnMayBeCapturedEnPassantRulesEngine();
    private boolean                               mayBeCapturedEnPassant;

    public Pawn(PlayerColor color) {
        super(color);
    }

    @Override
    public String getName() {
        return PAWN;
    }

    @Override
    public PieceType getNextPawnPromotion() {
        return new Queen(getColor());
    }

    @Override
    public int getValue() {
        return PIECE_VALUE;
    }

    @Override
    public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
        return pawnIsValidMoveRulesEngine.process(isValidMoveParameter);
    }

    @Override
    public void setPieceTypeSpecificAttributes(@NotNull Game game, @NotNull Field from, Field to) {
        ((Pawn) from.getPieceType()).setMayBeCapturedEnPassant(game.getChessboard(), from, to).setPawnBeingPromoted(from, to);

        if ((from.getPieceType()).isPawnBeingPromoted()) {
            to.setPieceType(getNextPawnPromotion());
        }
    }

    /**
     * Sets whether the pawn may be captured en passant or not.
     *
     * @param chessboard The backend representation of a chessboard.
     * @param from The field from which the pawn is moving.
     * @param to   The field to which the pawn is moving.
     * @return The pawn.
     */
    public Pawn setMayBeCapturedEnPassant(Chessboard chessboard, Field from, Field to) {
        setMayBeCapturedEnPassant(pawnMayBeCapturedEnPassantRulesEngine.process(new IsValidMoveParameter(chessboard, from, to, false)));

        return this;
    }

    /**
     * Sets whether the pawn is being promoted or not.
     *
     * @param from The field from which the pawn is moving.
     * @param to   The field to which the pawn is moving.
     * @return The pawn.
     */
    public Pawn setPawnBeingPromoted(@NotNull Field from, Field to) {
        setPawnBeingPromoted(from.getPieceType().isPawnBeingPromoted() ||
                from.getCoordinates().getY() == from.getPieceType().getColor().getOpposite().getBaseline() ||
                to.getCoordinates().getY() == from.getPieceType().getColor().getOpposite().getBaseline());

        return this;
    }
}
