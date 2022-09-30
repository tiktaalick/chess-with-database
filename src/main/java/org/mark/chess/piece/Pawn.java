package org.mark.chess.piece;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.game.Game;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.PawnIsValidMoveRulesEngine;
import org.mark.chess.piece.maybecapturedenpassant.PawnMayBeCapturedEnPassantRulesEngine;
import org.mark.chess.player.PlayerColor;

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
    public void setPieceTypeSpecificAttributes(Game game, Field from, Field to) {
        ((Pawn) from.getPieceType()).setMayBeCapturedEnPassant(game.getGrid(), from, to).setPawnBeingPromoted(from, to);

        if ((from.getPieceType()).isPawnBeingPromoted()) {
            to.addChessPiece(getNextPawnPromotion());
        }
    }

    public Pawn setMayBeCapturedEnPassant(Grid grid, Field from, Field to) {
        setMayBeCapturedEnPassant(pawnMayBeCapturedEnPassantRulesEngine.process(new IsValidMoveParameter(grid, from, to, false)));

        return this;
    }

    public Pawn setPawnBeingPromoted(Field from, Field to) {
        setPawnBeingPromoted(from.getPieceType().isPawnBeingPromoted() ||
                from.getCoordinates().getY() == from.getPieceType().getColor().getOpposite().getBaseline() ||
                to.getCoordinates().getY() == from.getPieceType().getColor().getOpposite().getBaseline());

        return this;
    }
}
