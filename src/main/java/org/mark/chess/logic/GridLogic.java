package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.model.*;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GridLogic {
    private static final int COLUMNS = 8;
    private static final int ROWS = 8;
    private static final double RELATIVE_IMAGE_SIZE = .8;
    private static final String IMAGES = "src/main/resources/images/";
    private static final String UNDERSCORE = "_";

    @Autowired
    private FieldLogic fieldLogic;

    public List<Field> initializeGrid(Board board) {
        List<Field> grid = new ArrayList<>();

        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                grid.add(fieldLogic.initializeField(board, row, column, COLUMNS));
            }
        }

        addChessPieces(grid);

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

    public void addChessPieces(List<Field> grid) {
        addChessPiece(grid, 0, new Rook(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(grid, 1, new Knight(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(grid, 2, new Bishop(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(grid, 3, new Queen(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(grid, 4, new King(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(grid, 5, new Bishop(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(grid, 6, new Knight(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(grid, 7, new Rook(), org.mark.chess.enums.Color.BLACK);

        for (int i = 8; i < 16; i++) {
            addChessPiece(grid, i, new Pawn(), org.mark.chess.enums.Color.BLACK);
        }

        for (int i = 48; i < 56; i++) {
            addChessPiece(grid, i, new Pawn(), org.mark.chess.enums.Color.WHITE);
        }

        addChessPiece(grid, 56, new Rook(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(grid, 57, new Knight(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(grid, 58, new Bishop(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(grid, 59, new Queen(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(grid, 60, new King(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(grid, 61, new Bishop(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(grid, 62, new Knight(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(grid, 63, new Rook(), org.mark.chess.enums.Color.WHITE);
    }

    public void addChessPiece(List<Field> grid, int index, Piece piece, org.mark.chess.enums.Color color) {
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
