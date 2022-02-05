package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.model.*;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FieldLogic {
    private static final double RELATIVE_IMAGE_SIZE = .8;
    private static final String IMAGES = "src/main/resources/images/";
    private static final String UNDERSCORE = "_";

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

    public void addChessPieces(List<Field> grid) {
        addChessPiece(grid, 0, new Rook(), Color.BLACK);
        addChessPiece(grid, 1, new Knight(), Color.BLACK);
        addChessPiece(grid, 2, new Bishop(), Color.BLACK);
        addChessPiece(grid, 3, new Queen(), Color.BLACK);
        addChessPiece(grid, 4, new King(), Color.BLACK);
        addChessPiece(grid, 5, new Bishop(), Color.BLACK);
        addChessPiece(grid, 6, new Knight(), Color.BLACK);
        addChessPiece(grid, 7, new Rook(), Color.BLACK);

        for (int i = 8; i < 16; i++) {
            addChessPiece(grid, i, new Pawn(), Color.BLACK);
        }

        for (int i = 48; i < 56; i++) {
            addChessPiece(grid, i, new Pawn(), Color.WHITE);
        }

        addChessPiece(grid, 56, new Rook(), Color.WHITE);
        addChessPiece(grid, 57, new Knight(), Color.WHITE);
        addChessPiece(grid, 58, new Bishop(), Color.WHITE);
        addChessPiece(grid, 59, new Queen(), Color.WHITE);
        addChessPiece(grid, 60, new King(), Color.WHITE);
        addChessPiece(grid, 61, new Bishop(), Color.WHITE);
        addChessPiece(grid, 62, new Knight(), Color.WHITE);
        addChessPiece(grid, 63, new Rook(), Color.WHITE);
    }

    private void addChessPiece(List<Field> grid, int index, Piece piece, Color color) {
        JButton button = grid.get(index).button();
        button.setEnabled(true);
        button.setToolTipText(color.getName() + " " + piece.pieceType().getName());
        button.setText(null);

        button.setIcon(
                new ImageIcon(new ImageIcon(IMAGES + color.getName() + UNDERSCORE + piece.pieceType().getName() +
                        ".png")
                        .getImage()
                        .getScaledInstance(
                                (int) (button.getWidth() * RELATIVE_IMAGE_SIZE),
                                (int) (button.getHeight() * RELATIVE_IMAGE_SIZE),
                                Image.SCALE_SMOOTH)));
        button.setBorder(null);

        grid.set(index, grid.get(index)
                .piece(piece.color(color).kickedOff(false))
                .button(button));
    }

    public Field getKingField(List<Field> grid, Color color) {
        return grid.stream()
                .filter(field -> field.piece() != null)
                .filter(field -> field.piece().color() == color)
                .filter(field -> field.piece().pieceType() == PieceType.KING)
                .findFirst()
                .orElse(new Field());
    }
}