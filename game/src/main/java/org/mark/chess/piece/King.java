package org.mark.chess.piece;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.KingIsValidMoveRulesEngine;
import org.mark.chess.player.PlayerColor;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class King extends PieceType {

    private static final int                        PIECE_VALUE                = 0;
    private static final KingIsValidMoveRulesEngine kingIsValidMoveRulesEngine = new KingIsValidMoveRulesEngine();

    private boolean hasMovedAtLeastOnce;

    public King(PlayerColor color) {
        super(color);
    }

    @Override
    public String getName() {
        return KING;
    }

    @Override
    public PieceType getNextPawnPromotion() {
        throw new IllegalPawnPromotionException();
    }

    @Override
    public int getValue() {
        return PIECE_VALUE;
    }

    @Override
    public boolean isValidMove(IsValidMoveParameter isValidMoveParameter) {
        return kingIsValidMoveRulesEngine.process(isValidMoveParameter);
    }

    @Override
    public void setPieceTypeSpecificAttributes(Game game, @NotNull Field from, Field to) {
        ((King) from.getPieceType()).setHasMovedAtLeastOnce(true);
    }
}
