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

    public List<Field> initializeGrid(Board board) {
        List<Field> grid = new ArrayList<>();

        for (int row = 0; row < NUMBER_OF_COLUMNS_AND_ROWS; row++) {
            for (int column = 0; column < NUMBER_OF_COLUMNS_AND_ROWS; column++) {
                grid.add(fieldLogic.initializeField(board, row, column));
            }
        }

        return grid;
    }

    public void addChessPieces(Game game) {
        fieldLogic.addChessPiece(game, "a8", new Rook(), Color.BLACK);
        fieldLogic.addChessPiece(game, "b8", new Knight(), Color.BLACK);
        fieldLogic.addChessPiece(game, "c8", new Bishop(), Color.BLACK);
        fieldLogic.addChessPiece(game, "d8", new Queen(), Color.BLACK);
        fieldLogic.addChessPiece(game, "e8", new King(), Color.BLACK);
        fieldLogic.addChessPiece(game, "f8", new Bishop(), Color.BLACK);
        fieldLogic.addChessPiece(game, "g8", new Knight(), Color.BLACK);
        fieldLogic.addChessPiece(game, "h8", new Rook(), Color.BLACK);
        fieldLogic.addChessPiece(game, "a7", new Pawn(), Color.BLACK);
        fieldLogic.addChessPiece(game, "b7", new Pawn(), Color.BLACK);
        fieldLogic.addChessPiece(game, "c7", new Pawn(), Color.BLACK);
        fieldLogic.addChessPiece(game, "d7", new Pawn(), Color.BLACK);
        fieldLogic.addChessPiece(game, "e7", new Pawn(), Color.BLACK);
        fieldLogic.addChessPiece(game, "f7", new Pawn(), Color.BLACK);
        fieldLogic.addChessPiece(game, "g7", new Pawn(), Color.BLACK);
        fieldLogic.addChessPiece(game, "h7", new Pawn(), Color.BLACK);
        fieldLogic.addChessPiece(game, "a2", new Pawn(), Color.WHITE);
        fieldLogic.addChessPiece(game, "b2", new Pawn(), Color.WHITE);
        fieldLogic.addChessPiece(game, "c2", new Pawn(), Color.WHITE);
        fieldLogic.addChessPiece(game, "d2", new Pawn(), Color.WHITE);
        fieldLogic.addChessPiece(game, "e2", new Pawn(), Color.WHITE);
        fieldLogic.addChessPiece(game, "f2", new Pawn(), Color.WHITE);
        fieldLogic.addChessPiece(game, "g2", new Pawn(), Color.WHITE);
        fieldLogic.addChessPiece(game, "h2", new Pawn(), Color.WHITE);
        fieldLogic.addChessPiece(game, "a1", new Rook(), Color.WHITE);
        fieldLogic.addChessPiece(game, "b1", new Knight(), Color.WHITE);
        fieldLogic.addChessPiece(game, "c1", new Bishop(), Color.WHITE);
        fieldLogic.addChessPiece(game, "d1", new Queen(), Color.WHITE);
        fieldLogic.addChessPiece(game, "e1", new King(), Color.WHITE);
        fieldLogic.addChessPiece(game, "f1", new Bishop(), Color.WHITE);
        fieldLogic.addChessPiece(game, "g1", new Knight(), Color.WHITE);
        fieldLogic.addChessPiece(game, "h1", new Rook(), Color.WHITE);
    }

    public GridLayout createGridLayout() {
        return new GridLayout(NUMBER_OF_COLUMNS_AND_ROWS, NUMBER_OF_COLUMNS_AND_ROWS);
    }

    public Field getField(List<Field> grid, JButton button) {
        return grid.stream().filter(field -> field.getButton() == button).findFirst().orElse(null);
    }

    public Field getField(List<Field> grid, Coordinates coordinates) {
        return grid
                .stream()
                .filter(field -> field.getCoordinates().getX() == coordinates.getX())
                .filter(field -> field.getCoordinates().getY() == coordinates.getY())
                .findFirst()
                .orElse(null);
    }

    public Field getKingField(List<Field> grid, Color color) {
        return grid
                .stream()
                .filter(field -> field.getPiece() != null)
                .filter(field -> field.getPiece().getColor() == color)
                .filter(field -> field.getPiece().getPieceType() == PieceType.KING)
                .findFirst()
                .orElse(null);
    }
}
