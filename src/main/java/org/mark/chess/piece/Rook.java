package org.mark.chess.piece;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.RookIsValidMoveRulesEngine;
import org.mark.chess.player.PlayerColor;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Rook extends PieceType {

    private static final int                        PIECE_VALUE                = 5;
    private static final RookIsValidMoveRulesEngine rookIsValidMoveRulesEngine = new RookIsValidMoveRulesEngine();

    private boolean hasMovedAtLeastOnce;

    public Rook(PlayerColor color) {
        super(color);
    }

    @Override
    public String getName() {
        return ROOK;
    }

    @Override
    public PieceType getNextPawnPromotion() {
        return new Bishop(getColor());
    }

    @Override
    public int getValue() {
        return PIECE_VALUE;
    }

    @Override
    public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
        return rookIsValidMoveRulesEngine.process(isValidMoveParameter);
    }

    @Override
    public void setPieceTypeSpecificAttributes(Game game, Field from, Field to) {
        ((Rook) from.getPieceType()).setHasMovedAtLeastOnce(true);
    }
}
