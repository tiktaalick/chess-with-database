package org.mark.chess.piece;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.KnightIsValidMoveRulesEngine;
import org.mark.chess.player.PlayerColor;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Knight extends PieceType {

    private static final int                          PIECE_VALUE                  = 3;
    private static final KnightIsValidMoveRulesEngine knightIsValidMoveRulesEngine = new KnightIsValidMoveRulesEngine();

    public Knight(PlayerColor color) {
        super(color);
    }

    @Override
    public String getName() {
        return KNIGHT;
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
        return knightIsValidMoveRulesEngine.process(isValidMoveParameter);
    }

    @Override
    public void setPieceTypeSpecificAttributes(Game game, Field from, Field to) {
        // No specific fields for knight need to be set.
    }
}
