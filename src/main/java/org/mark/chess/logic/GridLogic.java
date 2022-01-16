package org.mark.chess.logic;

import org.mark.chess.model.Field;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GridLogic {
    private static final int COLUMNS = 8;
    private static final int ROWS = 8;

    @Autowired
    FieldLogic fieldLogic;

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

}
