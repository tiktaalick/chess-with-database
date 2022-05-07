package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.InitialPieceFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.JButton;
import java.awt.GridLayout;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mark.chess.enums.Color.WHITE;

public class GridLogic {
    public static final int NUMBER_OF_COLUMNS_AND_ROWS = 8;

    @Autowired
    private FieldLogic fieldLogic;

    @Autowired
    private InitialPieceFactory initialPieceFactory;

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

    public List<Field> initializeGrid(Game game, Board board) {
        return IntStream
                .rangeClosed(0, 63)
                .map(id -> game.getHumanPlayerColor() == WHITE
                        ? id
                        : 63 - id)
                .mapToObj(id -> fieldLogic.addChessPiece(fieldLogic.initializeField(board, id), initialPieceFactory.getInitialPiece(id)))
                .collect(Collectors.toList());
    }
}
