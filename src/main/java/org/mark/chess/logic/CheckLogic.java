package org.mark.chess.logic;

import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.PieceTypeLogic;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckLogic {
    private final ColorLogic     colorLogic;
    private final PieceTypeLogic pieceTypeLogic;

    public CheckLogic(PieceTypeLogic pieceTypeLogic, ColorLogic colorLogic) {
        this.pieceTypeLogic = pieceTypeLogic;
        this.colorLogic = colorLogic;
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
                .filter(opponentField -> opponentField.getPiece().getPieceType().getLogic(pieceTypeLogic).isValidMove(grid, opponentField, to, true))
                .collect(Collectors.toList());

        attackers.forEach((Field field) -> colorLogic.setAttacking(grid, field));

        return !attackers.isEmpty();
    }

    public boolean isMovingIntoCheck(Grid grid, Field from, Field to, boolean isOpponent, GridLogic gridLogic) {
        if (isOpponent) {
            return false;
        }

        var gridAfterMovement = Grid.createGrid(grid, from, to, gridLogic);

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
        return opponentField
                .getPiece()
                .getPieceType()
                .getLogic(pieceTypeLogic)
                .isValidMove(gridAfterMovement, opponentField, gridAfterMovement.getKingField(), true);
    }
}
