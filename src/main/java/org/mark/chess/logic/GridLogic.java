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

    @Autowired
    private FieldLogic fieldLogic;

    public List<Field> initializeGrid(Game game, Board board) {
        List<Field> grid = new ArrayList<>();

        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                grid.add(fieldLogic.initializeField(board, row, column, COLUMNS));
            }
        }

        addChessPieces(game.grid(grid));

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

    public void addChessPieces(Game game) {
        addChessPiece(game, 0, new Rook(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(game, 1, new Knight(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(game, 2, new Bishop(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(game, 3, new Queen(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(game, 4, new King(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(game, 5, new Bishop(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(game, 6, new Knight(), org.mark.chess.enums.Color.BLACK);
        addChessPiece(game, 7, new Rook(), org.mark.chess.enums.Color.BLACK);

        for (int i = 8; i < 16; i++) {
            addChessPiece(game, i, new Pawn(), org.mark.chess.enums.Color.BLACK);
        }

        for (int i = 48; i < 56; i++) {
            addChessPiece(game, i, new Pawn(), org.mark.chess.enums.Color.WHITE);
        }

        addChessPiece(game, 56, new Rook(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(game, 57, new Knight(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(game, 58, new Bishop(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(game, 59, new Queen(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(game, 60, new King(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(game, 61, new Bishop(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(game, 62, new Knight(), org.mark.chess.enums.Color.WHITE);
        addChessPiece(game, 63, new Rook(), org.mark.chess.enums.Color.WHITE);
    }

    public void addChessPiece(Game game, int index, Piece piece, Color color) {
        game.grid().set(index, game.grid().get(index)
                .piece(piece.color(color))
                .button(fieldLogic.initializeButton(game, index)));
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
