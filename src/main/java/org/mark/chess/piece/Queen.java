package org.mark.chess.piece;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.QueenIsValidMoveRulesEngine;
import org.mark.chess.player.PlayerColor;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Queen extends PieceType {

    private static final int                         PIECE_VALUE                 = 9;
    private static final QueenIsValidMoveRulesEngine queenIsValidMoveRulesEngine = new QueenIsValidMoveRulesEngine();

    public Queen(PlayerColor color) {
        super(color);
    }

    @Override
    public String getName() {
        return QUEEN;
    }

    @Override
    public PieceType getNextPawnPromotion() {
        return new Rook(getColor());
    }

    @Override
    public int getValue() {
        return PIECE_VALUE;
    }

    @Override
    public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
        return queenIsValidMoveRulesEngine.process(isValidMoveParameter);
    }

    @Override
    public void setPieceTypeSpecificFields(Game game, Field from, Field to) {
        // No specific fields for queen need to be set.
    }
}
