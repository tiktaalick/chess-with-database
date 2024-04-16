package org.mark.chess.piece.queen;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.general.PieceType;
import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.queen.isvalidmove.QueenIsValidMoveRulesEngine;
import org.mark.chess.piece.rook.Rook;
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
    public void setPieceTypeSpecificAttributes(Game game, Field from, Field to) {
        // No specific fields for queen need to be set.
    }
}
