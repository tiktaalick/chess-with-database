package org.mark.chess.logic;

import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Piece;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.mark.chess.logic.GridLogic.NUMBER_OF_COLUMNS_AND_ROWS;
import static org.mark.chess.logic.PieceLogic.backgroundColorFactory;

public class FieldLogic {
    @Autowired
    private ButtonLogic buttonLogic;

    public Field addChessPiece(Field field, Piece piece) {
        return field.setPiece(piece).setButton(buttonLogic.initializeButton(field));
    }

    public String createCode(int id) {
        return createCode(id % NUMBER_OF_COLUMNS_AND_ROWS + 1, 8 - id / NUMBER_OF_COLUMNS_AND_ROWS);
    }

    public String createCode(Coordinates coordinates) {
        return createCode(coordinates.getX(), coordinates.getY());
    }

    public String createCode(int x, int y) {
        return ((char) ('a' + x - 1)) + String.valueOf(y);
    }

    public Coordinates createCoordinates(int id) {
        return new Coordinates(id % NUMBER_OF_COLUMNS_AND_ROWS + 1, 8 - id / NUMBER_OF_COLUMNS_AND_ROWS);
    }

    public Coordinates createCoordinates(String code) {
        return createCoordinates(createId(code));
    }

    public int createId(Coordinates coordinates) {
        return coordinates.getX() + coordinates.getY() * NUMBER_OF_COLUMNS_AND_ROWS;
    }

    public int createId(String code) {
        return (code.charAt(0) - 'a') + (8 - Integer.parseInt(code.substring(1))) * NUMBER_OF_COLUMNS_AND_ROWS;
    }

    public int getMaxValue(List<Field> grid) {
        return grid == null
                ? 0
                : grid.stream().mapToInt(Field::getValue).max().orElse(0);
    }

    public int getMinValue(List<Field> grid) {
        return grid == null
                ? 0
                : grid.stream().mapToInt(Field::getValue).min().orElse(0);
    }

    public int getValue(Field field) {
        return Optional.ofNullable(field.getPiece()).map(piece -> piece.getPieceType().getValue()).orElse(0);
    }

    public Field initializeField(Board board, int id) {
        Field field = new Field().setId(id);
        field.setButton(new Button(board, field));
        board.add(field.getButton());

        return field;
    }

    public boolean isActivePlayerField(Game game, Field field) {
        return field.getPiece() != null && field.getPiece().getColor() == game.getCurrentPlayerColor();
    }

    public Field setValue(Game game, Field field) {
        field.setValue(getValue(field));

        if (game.getGrid() != null) {
            int minValue = getMinValue(game.getGrid().getFields());
            int maxValue = getMaxValue(game.getGrid().getFields());
            game.getGrid().getFields().stream().filter(gridField -> gridField.getPiece() != null).forEach(gridField -> {
                double relativeValue = (((double) gridField.getValue() - minValue) / (Math.max(1, maxValue - minValue))) * 255;
                gridField.setRelativeValue((int) relativeValue);
                gridField.getButton().setBackground(backgroundColorFactory.getBackgroundColor(gridField));
            });
        }

        return field;
    }
}

