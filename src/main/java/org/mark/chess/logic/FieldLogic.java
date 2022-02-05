package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;

public class FieldLogic {
    public Field initializeField(Board board, int row, int column, int numberOfColumns) {
        int id = createId(row, column, numberOfColumns);

        Field field = new Field()
                .id(id)
                .coordinates(new Coordinates(column, row))
                .button(new Button(
                        column * Button.FIELD_WIDTH,
                        row * Button.FIELD_WIDTH,
                        (id + row) % 2 == 0 ? Color.LIGHT.getAwtColor() : Color.DARK.getAwtColor(),
                        String.valueOf(id),
                        board,
                        board));
        field.button().setEnabled(false);
        board.add(field.button());

        return field;
    }

    private int createId(int row, int column, int numberOfColumns) {
        return row * numberOfColumns + column;
    }
}