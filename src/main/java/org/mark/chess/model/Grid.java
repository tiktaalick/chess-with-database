package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.enums.Color;
import org.mark.chess.logic.GridLogic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)

public final class Grid {
    private GridLogic   gridLogic;
    private List<Field> fields;
    private Field       kingField;
    private Field       opponentKingField;
    private int         gridValue;

    private Grid(List<Field> fields, GridLogic gridLogic) {
        this.fields = Collections.unmodifiableList(fields);
        this.gridLogic = gridLogic;
        this.kingField = gridLogic.getKingField(this, Color.WHITE);
        this.opponentKingField = gridLogic.getKingField(this, Color.BLACK);
        this.gridValue = gridLogic.calculateGridValue(this, Color.WHITE);
    }

    private Grid(Grid gridBeforeTheMove, Field from, Field to, GridLogic gridLogic) {
        this.gridLogic = gridLogic;
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
                        ? new Field(null).setCoordinates(from.getCoordinates())
                        : new Field(from.getPiece()).setCoordinates(to.getCoordinates()))
                .collect(Collectors.toList());

        this.fields.addAll(movementList);

        this.kingField = gridLogic.getKingField(this, from.getPiece().getColor());
        this.opponentKingField = gridLogic.getKingField(this, from.getPiece().getColor().getOpposite());
        this.gridValue = gridLogic.calculateGridValue(this, from.getPiece().getColor());
    }

    public static Grid createGrid(Grid gridBeforeTheMove, Field from, Field to, GridLogic gridLogic) {
        return new Grid(gridBeforeTheMove, from, to, gridLogic);
    }

    public static Grid createGrid(List<Field> fields, GridLogic gridLogic) {
        return new Grid(fields, gridLogic);
    }
}
