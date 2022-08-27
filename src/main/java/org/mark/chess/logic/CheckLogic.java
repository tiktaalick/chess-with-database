package org.mark.chess.logic;

import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CheckLogic {
    private PieceLogicFactory pieceLogicFactory;

    @Autowired
    public CheckLogic(PieceLogicFactory pieceLogicFactory) {
        this.pieceLogicFactory = pieceLogicFactory;
    }

    public boolean isInCheckNow(Grid grid, Field from, Field to, boolean isOpponent) {
        if (isOpponent) {
            return false;
        }

        grid.getFields().forEach(field -> field.setUnderAttack(false));

        List<Field> attackers = grid
                .getFields()
                .stream()
                .filter(opponentField -> null != opponentField.getPiece())
                .filter(opponentField -> opponentField.getPiece().getColor() != from.getPiece().getColor())
                .filter(opponentField -> pieceLogicFactory
                        .getLogic(opponentField.getPiece().getPieceType())
                        .isValidMove(grid, opponentField, to, true))
                .collect(Collectors.toList());

        attackers.forEach(field -> {
            field.setAttacking(true).getButton().setBackground(BackgroundColorFactory.getBackgroundColor(field));
            grid
                    .getOpponentKingField()
                    .setUnderAttack(true)
                    .getButton()
                    .setBackground(BackgroundColorFactory.getBackgroundColor(grid.getOpponentKingField()));
        });

        return !attackers.isEmpty();
    }

    public boolean isMovingIntoCheck(Grid grid, Field from, Field to, boolean isOpponent, GridLogic gridLogic) {
        if (isOpponent) {
            return false;
        }

        Grid gridAfterMovement = new Grid(grid, from, to, gridLogic);

        List<Field> attackers = gridAfterMovement
                .getFields()
                .stream()
                .filter(opponentField -> opponentField.getPiece() != null)
                .filter(opponentField -> opponentField.getPiece().getColor() != from.getPiece().getColor())
                .filter(opponentField -> pieceLogicFactory
                        .getLogic(opponentField.getPiece().getPieceType())
                        .isValidMove(gridAfterMovement, opponentField, gridAfterMovement.getKingField(), true))
                .collect(Collectors.toList());

        return !attackers.isEmpty();
    }
}
