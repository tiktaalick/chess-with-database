package org.mark.chess.logic;

import org.mark.chess.enums.Color;
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

    public Field initializeField(Board board, int row, int column) {
        Field field = new Field().setCode(createCode(row, column)).setCoordinates(new Coordinates(column, row));
        field.setButton(new Button(board, field));
        field.getButton().setEnabled(false);
        board.add(field.getButton());

        return field;
    }

    public void addChessPiece(Game game, String code, Piece piece, Color color) {
        int id = createId(code);
        game.getGrid().set(id, game.getGrid().get(id).setPiece(piece.setColor(color)).setButton(buttonLogic.initializeButton(game, id)));
    }

    public int createId(String code) {
        return (8 - Integer.parseInt(code.substring(1))) * NUMBER_OF_COLUMNS_AND_ROWS + (code.charAt(0) - 'a');
    }

    public String createCode(int id) {
        return createCode(id / NUMBER_OF_COLUMNS_AND_ROWS, id % NUMBER_OF_COLUMNS_AND_ROWS);
    }

    public String createCode(int row, int column) {
        return ((char) ('a' + column)) + String.valueOf(8 - row);
    }
}