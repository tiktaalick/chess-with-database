package org.mark.chess.piece;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.isvalidmove.BishopIsValidMoveRulesEngine;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.player.PlayerColor;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Bishop extends PieceType {

    private static final int                          PIECE_VALUE                  = 3;
    private static final BishopIsValidMoveRulesEngine bishopIsValidMoveRulesEngine = new BishopIsValidMoveRulesEngine();

    public Bishop(PlayerColor color) {
        super(color);
    }

    @Override
    public String getName() {
        return BISHOP;
    }

    @Override
    public PieceType getNextPawnPromotion() {
        return new Knight(getColor());
    }

    @Override
    public int getValue() {
        return PIECE_VALUE;
    }

    @Override
    public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
        return bishopIsValidMoveRulesEngine.process(isValidMoveParameter);
    }

    @Override
    public void setPieceTypeSpecificAttributes(Game game, Field from, Field to) {
        // No specific fields for bishop need to be set.
    }
}
