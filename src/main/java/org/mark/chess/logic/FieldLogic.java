package org.mark.chess.logic;

import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Piece;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mark.chess.logic.GridLogic.NUMBER_OF_COLUMNS_AND_ROWS;

public class FieldLogic {
    @Autowired
    private ButtonLogic buttonLogic;

    public Field initializeField(Board board, int id) {
        Field field = new Field().setId(id);
        field.setButton(new Button(board, field));
        field.getButton().setEnabled(false);
        board.add(field.getButton());

        return field;
    }

    public Field addChessPiece(Game game, Field field, Piece piece) {
        return field.setPiece(piece).setButton(buttonLogic.initializeButton(game, field));
    }

    public int createId(String code) {
        return (code.charAt(0) - 'a') + (8 - Integer.parseInt(code.substring(1))) * NUMBER_OF_COLUMNS_AND_ROWS;
    }

    public int createId(Coordinates coordinates) {
        return coordinates.getX() + coordinates.getY() * NUMBER_OF_COLUMNS_AND_ROWS;
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
}