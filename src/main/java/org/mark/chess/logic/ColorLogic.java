package org.mark.chess.logic;

import org.jetbrains.annotations.NotNull;
import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Grid;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
class ColorLogic {
    private final GameLogic      gameLogic;
    private final GridValueLogic gridValueLogic;

    @Lazy
    ColorLogic(GameLogic gameLogic, GridValueLogic gridValueLogic) {
        this.gameLogic = gameLogic;
        this.gridValueLogic = gridValueLogic;
    }

    void setAttacking(Grid grid, @NotNull Field field) {
        field.setAttacking(true).getButton().setBackground(BackgroundColorFactory.getBackgroundColor(field));
        grid
                .getOpponentKingField()
                .setUnderAttack(true)
                .getButton()
                .setBackground(BackgroundColorFactory.getBackgroundColor(grid.getOpponentKingField()));
    }

    void setKingFieldColors(Game game, Collection<Field> allValidMoves) {
        game.getGrid().getFields().stream().filter(field -> field.getPiece() != null).forEach((Field field) -> {
            if (field.getPiece().getPieceType() == PieceType.KING) {
                game.getGrid().setKingFieldFlags(game, allValidMoves, field);
                gameLogic.setGameProgress(game, field);
            }

            if (!game.isInProgress()) {
                field.getButton().setBackground(BackgroundColorFactory.getBackgroundColor(field));
            }
        });
    }

    void setValidMoveColors(Grid grid, Field from, Collection<Field> validMoves, Collection<Field> allValidMoves) {
        grid.getFields().forEach(field -> field.setValue(null).setRelativeValue(null));
        allValidMoves.forEach(to -> gridValueLogic.createAbsoluteFieldValues(grid, from, to));
        gridValueLogic.createRelativeFieldValues(validMoves, allValidMoves, from);
    }
}
