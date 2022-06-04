package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.enums.Color;
import org.mark.chess.logic.GridLogic;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class Grid {
    private GridLogic gridLogic = new GridLogic();

    private List<Field> fields;
    private Field       kingField;
    private Field       opponentKingField;
    private int         gridValue;

    public Grid(List<Field> fields) {
        this.fields = fields;
        this.kingField = gridLogic.getKingField(this, Color.WHITE);
        this.opponentKingField = gridLogic.getKingField(this, Color.BLACK);
        this.gridValue = gridLogic.calculateGridValue(this, Color.WHITE);
    }

    public Grid(Grid gridBeforeTheMove, Field from, Field to) {
        this.fields = gridBeforeTheMove
                .getFields()
                .stream()
                .filter(field -> !Arrays.asList(from.getCode(), to.getCode()).contains(field.getCode()))
                .collect(Collectors.toList());

        List<Field> movementList = gridBeforeTheMove
                .getFields()
                .stream()
                .filter(field -> Arrays.asList(from.getCode(), to.getCode()).contains(field.getCode()))
                .map(field -> Objects.equals(field.getCode(), from.getCode())
                        ? new Field().setCoordinates(from.getCoordinates())
                        : new Field().setCoordinates(to.getCoordinates()).setPiece(from.getPiece()))
                .collect(Collectors.toList());

        this.fields.addAll(movementList);

        this.kingField = gridLogic.getKingField(this, from.getPiece().getColor());
        this.opponentKingField = gridLogic.getKingField(this, from.getPiece().getColor().getOpposite());
        this.gridValue = gridLogic.calculateGridValue(this, from.getPiece().getColor());
    }
}
