package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.model.Bishop;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.King;
import org.mark.chess.model.Knight;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.JButton;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class GridLogic {
    public static final int NUMBER_OF_COLUMNS_AND_ROWS = 8;

    @Autowired
    private FieldLogic fieldLogic;

    public List<Field> initializeGrid(Game game, Board board) {
        List<Field> grid = new ArrayList<>();

        for (int row = 0; row < NUMBER_OF_COLUMNS_AND_ROWS; row++) {
            for (int column = 0; column < NUMBER_OF_COLUMNS_AND_ROWS; column++) {
                grid.add(fieldLogic.initializeField(board, row, column));
            }
        }

        addChessPieces(game.grid(grid));

        return grid;
    }

    public void addChessPieces(Game game) {
        addChessPiece(game, 0, new Rook(), Color.BLACK);
        addChessPiece(game, 1, new Knight(), Color.BLACK);
        addChessPiece(game, 2, new Bishop(), Color.BLACK);
        addChessPiece(game, 3, new Queen(), Color.BLACK);
        addChessPiece(game, 4, new King(), Color.BLACK);
        addChessPiece(game, 5, new Bishop(), Color.BLACK);
        addChessPiece(game, 6, new Knight(), Color.BLACK);
        addChessPiece(game, 7, new Rook(), Color.BLACK);

        for (int i = 8; i < 16; i++) {
            addChessPiece(game, i, new Pawn(), Color.BLACK);
        }

        for (int i = 48; i < 56; i++) {
            addChessPiece(game, i, new Pawn(), Color.WHITE);
        }

        addChessPiece(game, 56, new Rook(), Color.WHITE);
        addChessPiece(game, 57, new Knight(), Color.WHITE);
        addChessPiece(game, 58, new Bishop(), Color.WHITE);
        addChessPiece(game, 59, new Queen(), Color.WHITE);
        addChessPiece(game, 60, new King(), Color.WHITE);
        addChessPiece(game, 61, new Bishop(), Color.WHITE);
        addChessPiece(game, 62, new Knight(), Color.WHITE);
        addChessPiece(game, 63, new Rook(), Color.WHITE);
    }

    public void addChessPiece(Game game, int index, Piece piece, Color color) {
        game.grid().set(index, game.grid().get(index).piece(piece.color(color)).button(fieldLogic.initializeButton(game, index)));
    }

    public GridLayout createGridLayout() {
        return new GridLayout(NUMBER_OF_COLUMNS_AND_ROWS, NUMBER_OF_COLUMNS_AND_ROWS);
    }

    public Field getField(List<Field> grid, JButton button) {
        return grid.stream().filter(field -> field.button() == button).findFirst().orElse(new Field());
    }

    public Field getField(List<Field> grid, Coordinates coordinates) {
        return grid
                .stream()
                .filter(field -> field.coordinates().x() == coordinates.x())
                .filter(field -> field.coordinates().y() == coordinates.y())
                .findFirst()
                .orElse(new Field());
    }

    public Field getKingField(List<Field> grid, Color color) {
        return grid
                .stream()
                .filter(field -> field.piece() != null)
                .filter(field -> field.piece().color() == color)
                .filter(field -> field.piece().pieceType() == PieceType.KING)
                .findFirst()
                .orElse(new Field());
    }
}
