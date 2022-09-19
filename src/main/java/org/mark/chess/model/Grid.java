package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.swing.Button;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)

public final class Grid {
    public static final  int NUMBER_OF_COLUMNS_AND_ROWS = 8;
    private static final int MAXIMUM_SQUARE_ID          = 63;

    private List<Field> fields;
    private Field       kingField;
    private Field       opponentKingField;
    private int         gridValue;

    @Autowired
    public Grid(List<Field> fields) {
        this.fields = Collections.unmodifiableList(fields);
        this.kingField = getKingField(this, Color.WHITE);
        this.opponentKingField = getKingField(this, Color.BLACK);
        this.gridValue = calculateGridValue(this, Color.WHITE);
    }

    private Grid(Grid gridBeforeTheMove, Field from, Field to) {
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

        this.kingField = getKingField(this, from.getPiece().getColor());
        this.opponentKingField = getKingField(this, from.getPiece().getColor().getOpposite());
        this.gridValue = calculateGridValue(this, from.getPiece().getColor());
    }

    public static GridLayout createGridLayout() {
        return new GridLayout(NUMBER_OF_COLUMNS_AND_ROWS, NUMBER_OF_COLUMNS_AND_ROWS);
    }

    public int calculateGridValue(Grid grid, Color activePlayerColor) {
        return grid
                .getFields()
                .stream()
                .filter(field -> field.getPiece() != null)
                .mapToInt(field -> field.getPiece().getColor() == activePlayerColor
                        ? field.getPiece().getPieceType().getValue()
                        : -field.getPiece().getPieceType().getValue())
                .sum();
    }

    public Grid createGrid(Grid gridBeforeTheMove, Field from, Field to) {
        return new Grid(gridBeforeTheMove, from, to);
    }

    public Field getField(Coordinates coordinates) {
        return this
                .getFields()
                .stream()
                .filter(field -> field.getCoordinates().getX() == coordinates.getX())
                .filter(field -> field.getCoordinates().getY() == coordinates.getY())
                .findFirst()
                .orElse(null);
    }

    public Field getField(Button button) {
        return this.getFields().stream().filter(field -> button.equals(field.getButton())).findFirst().orElse(null);
    }

    public Field getKingField(Grid grid, Color color) {
        return grid
                .getFields()
                .stream()
                .filter(field -> field.getPiece() != null)
                .filter(field -> field.getPiece().getColor() == color)
                .filter(field -> field.getPiece().getPieceType() == PieceType.KING)
                .findFirst()
                .orElse(null);
    }

    public void setKingFieldFlags(Game game, Collection<Field> allValidMoves, Field kingField) {
        boolean isInCheckNow = kingField.isInCheckNow(game.getGrid(), false);
        kingField
                .setCheckMate(kingField.isNotAbleToMove(game, allValidMoves) && isInCheckNow)
                .setStaleMate(kingField.isNotAbleToMove(game, allValidMoves) && !isInCheckNow);
    }
}
