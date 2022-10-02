package org.mark.chess.board;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import static org.mark.chess.board.Grid.NUMBER_OF_COLUMNS_AND_ROWS;

/**
 * Contains methods for creating the {@link Coordinates}, id's and code of a field.
 */
@Data
@Accessors(chain = true)
public class Coordinates {

    private static final char FIRST_COLUMN_NAME = 'a';

    private int x;
    private int y;

    /**
     * Constructor.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates {@link Coordinates} based on an id.
     *
     * @param id The id of a field.
     * @return The {@link Coordinates}.
     */
    public static @NotNull Coordinates create(int id) {
        return new Coordinates(id % NUMBER_OF_COLUMNS_AND_ROWS + 1, NUMBER_OF_COLUMNS_AND_ROWS - id / NUMBER_OF_COLUMNS_AND_ROWS);
    }

    /**
     * Creates {@link Coordinates} based on a code.
     *
     * @param code The code of a field.
     * @return The {@link Coordinates}.
     */
    public static @NotNull Coordinates create(String code) {
        return create(createId(code));
    }

    /**
     * Creates a code based on an id.
     *
     * @param id The id of a field.
     * @return The code.
     */
    public static @NotNull String createCode(int id) {
        return createCode(id % NUMBER_OF_COLUMNS_AND_ROWS + 1, NUMBER_OF_COLUMNS_AND_ROWS - id / NUMBER_OF_COLUMNS_AND_ROWS);
    }

    /**
     * Creates a code based on {@link Coordinates}.
     *
     * @param coordinates The {@link Coordinates} of a field.
     * @return The code.
     */
    public static @NotNull String createCode(@NotNull Coordinates coordinates) {
        return createCode(coordinates.getX(), coordinates.getY());
    }

    /**
     * Creates a code based on the x and y coordinates.
     *
     * @param x The x-coordinate of a field.
     * @param y The y-coordinate of a field.
     * @return The code.
     */
    public static @NotNull String createCode(int x, int y) {
        return ((char) (FIRST_COLUMN_NAME + x - 1)) + String.valueOf(y);
    }

    /**
     * Creates an id based on {@link Coordinates}.
     *
     * @param coordinates The {@link Coordinates} of a field.
     * @return The id.
     */
    public static int createId(@NotNull Coordinates coordinates) {
        return coordinates.getX() + coordinates.getY() * NUMBER_OF_COLUMNS_AND_ROWS;
    }

    /**
     * Creates an id based on a code.
     *
     * @param code The code of a field.
     * @return The id.
     */
    public static int createId(@NotNull String code) {
        return (code.charAt(0) - FIRST_COLUMN_NAME) + (NUMBER_OF_COLUMNS_AND_ROWS - Integer.parseInt(code.substring(1))) * NUMBER_OF_COLUMNS_AND_ROWS;
    }
}
