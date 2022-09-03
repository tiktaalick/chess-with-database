package org.mark.chess.logic;

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
    private ColorLogic        colorLogic;
    private FieldLogic        fieldLogic;

    @Autowired
    public CheckLogic(PieceLogicFactory pieceLogicFactory, ColorLogic colorLogic, FieldLogic fieldLogic) {
        this.pieceLogicFactory = pieceLogicFactory;
        this.colorLogic = colorLogic;
        this.fieldLogic = fieldLogic;
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

        attackers.forEach((Field field) -> colorLogic.setAttacking(grid, field));

        return !attackers.isEmpty();
    }

    public boolean isMovingIntoCheck(Grid grid, Field from, Field to, boolean isOpponent, GridLogic gridLogic) {
        if (isOpponent) {
            return false;
        }

        var gridAfterMovement = Grid.createGrid(grid, from, to, gridLogic, fieldLogic);

        List<Field> attackers = gridAfterMovement
                .getFields()
                .stream()
                .filter(opponentField -> opponentField.getPiece() != null)
                .filter(opponentField -> opponentField.getPiece().getColor() != from.getPiece().getColor())
                .filter(opponentField -> isValidMove(gridAfterMovement, opponentField))
                .collect(Collectors.toList());

        return !attackers.isEmpty();
    }

    private boolean isValidMove(Grid gridAfterMovement, Field opponentField) {
        return pieceLogicFactory
                .getLogic(opponentField.getPiece().getPieceType())
                .isValidMove(gridAfterMovement, opponentField, gridAfterMovement.getKingField(), true);
    }
}
