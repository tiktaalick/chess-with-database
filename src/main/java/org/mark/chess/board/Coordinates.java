package org.mark.chess.board;

import lombok.Data;
import lombok.experimental.Accessors;

import static org.mark.chess.board.Grid.NUMBER_OF_COLUMNS_AND_ROWS;

@Data
@Accessors(chain = true)
public class Coordinates {

    private static final char FIRST_COLUMN_NAME = 'a';

    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinates create(int id) {
        return new Coordinates(id % NUMBER_OF_COLUMNS_AND_ROWS + 1, NUMBER_OF_COLUMNS_AND_ROWS - id / NUMBER_OF_COLUMNS_AND_ROWS);
    }

    public static Coordinates create(String code) {
        return create(createId(code));
    }

    public static String createCode(int id) {
        return createCode(id % NUMBER_OF_COLUMNS_AND_ROWS + 1, NUMBER_OF_COLUMNS_AND_ROWS - id / NUMBER_OF_COLUMNS_AND_ROWS);
    }

    public static String createCode(Coordinates coordinates) {
        return createCode(coordinates.getX(), coordinates.getY());
    }

    public static String createCode(int x, int y) {
        return ((char) (FIRST_COLUMN_NAME + x - 1)) + String.valueOf(y);
    }

    public static int createId(Coordinates coordinates) {
        return coordinates.getX() + coordinates.getY() * NUMBER_OF_COLUMNS_AND_ROWS;
    }

    public static int createId(String code) {
        return (code.charAt(0) - FIRST_COLUMN_NAME) + (NUMBER_OF_COLUMNS_AND_ROWS - Integer.parseInt(code.substring(1))) * NUMBER_OF_COLUMNS_AND_ROWS;
    }
}
