package org.mark.chess.logic;

import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Piece;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.springframework.stereotype.Service;

import static org.mark.chess.logic.GridLogic.NUMBER_OF_COLUMNS_AND_ROWS;

@Service
public class FieldLogic {
    private static final char FIRST_COLUMN_NAME = 'a';

    private final ButtonLogic buttonLogic;

    public FieldLogic(ButtonLogic buttonLogic) {
        this.buttonLogic = buttonLogic;
    }

    public String createCode(int id) {
        return createCode(id % NUMBER_OF_COLUMNS_AND_ROWS + 1, NUMBER_OF_COLUMNS_AND_ROWS - id / NUMBER_OF_COLUMNS_AND_ROWS);
    }

    public String createCode(Coordinates coordinates) {
        return createCode(coordinates.getX(), coordinates.getY());
    }

    public Coordinates createCoordinates(int id) {
        return new Coordinates(id % NUMBER_OF_COLUMNS_AND_ROWS + 1, NUMBER_OF_COLUMNS_AND_ROWS - id / NUMBER_OF_COLUMNS_AND_ROWS);
    }

    public Coordinates createCoordinates(String code) {
        return createCoordinates(createId(code));
    }

    public int createId(Coordinates coordinates) {
        return coordinates.getX() + coordinates.getY() * NUMBER_OF_COLUMNS_AND_ROWS;
    }

    public int createId(String code) {
        return (code.charAt(0) - FIRST_COLUMN_NAME) + (NUMBER_OF_COLUMNS_AND_ROWS - Integer.parseInt(code.substring(1))) * NUMBER_OF_COLUMNS_AND_ROWS;
    }

    Field addChessPiece(Field field, Piece piece) {
        return field.setPiece(piece).setButton(buttonLogic.initializeButton(field));
    }

    String createCode(int x, int y) {
        return ((char) (FIRST_COLUMN_NAME + x - 1)) + String.valueOf(y);
    }

    Field initializeField(Board board, int id) {
        var field = new Field(null).setId(id);
        field.setButton(new Button(board, field));
        board.add(field.getButton());

        return field;
    }

    boolean isActivePlayerField(Game game, Field field) {
        return field.getPiece() != null && field.getPiece().getColor() == game.getCurrentPlayerColor();
    }
}

