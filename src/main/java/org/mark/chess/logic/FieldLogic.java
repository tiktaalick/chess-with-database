package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.King;
import org.mark.chess.model.Piece;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FieldLogic {
    public Field initializeField(Board board, int row, int column, int numberOfColumns) {
        int id = createId(row, column, numberOfColumns);

        Field field = new Field()
                .id(id)
                .coordinates(new Coordinates(column, row))
                .button(new Button(
                        column * Button.FIELD_WIDTH,
                        row * Button.FIELD_WIDTH,
                        (id + row) % 2 == 0 ? Color.WHITE.getAwtColor() : Color.BLACK.getAwtColor(),
                        String.valueOf(id),
                        board,
                        board));
        board.add(field.button());

        return field;
    }

    private int createId(int row, int column, int numberOfColumns) {
        return row * numberOfColumns + column;
    }

    public void addChessPieces(List<Field> grid) {
        addChessPiece(grid, 4, new King(), Color.BLACK);
        addChessPiece(grid, 60, new King(), Color.WHITE);
    }

    private void addChessPiece(List<Field> grid, int index, Piece piece, Color color) {
        JButton button = grid.get(index).button();
        button.setToolTipText(color.getName() + " " + piece.pieceType().getName());
        button.setText(null);

        button.setIcon(
                new ImageIcon(new ImageIcon("src/main/resources/images/" + color.getName() + "_" + piece.pieceType().getName() +
                        ".png")
                        .getImage()
                        .getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH)));
        button.setBorder(null);

        grid.set(index, grid.get(index)
                .piece(piece.color(color).kickedOff(false))
                .button(button));
    }
}