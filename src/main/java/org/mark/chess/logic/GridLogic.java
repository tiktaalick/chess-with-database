package org.mark.chess.logic;

import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GridLogic {
    private static final int COLUMNS = 8;
    private static final int ROWS = 8;

    @Autowired
    private FieldLogic fieldLogic;

    public List<Field> initializeGrid(Board board) {
        List<Field> grid = new ArrayList<>();

        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                grid.add(fieldLogic.initializeField(board, row, column, COLUMNS));
            }
        }

        fieldLogic.addChessPieces(grid);

        return grid;
    }

    public GridLayout createGridLayout() {
        return new GridLayout(ROWS, COLUMNS);
    }

    public Field getField(List<Field> grid, JButton button) {
        return grid.stream()
                .filter(field -> field.button() == button)
                .findFirst()
                .orElse(new Field());
    }

    public Field getField(List<Field> grid, Coordinates coordinates) {
        return grid.stream()
                .filter(field -> field.coordinates().x() == coordinates.x())
                .filter(field -> field.coordinates().y() == coordinates.y())
                .findFirst()
                .orElse(new Field());
    }
}
