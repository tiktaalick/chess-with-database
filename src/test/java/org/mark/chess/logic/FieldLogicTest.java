package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.Color;
import org.mark.chess.model.Bishop;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Piece;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mark.chess.logic.GridLogic.NUMBER_OF_COLUMNS_AND_ROWS;
import static org.mark.chess.swing.Button.FIELD_WIDTH;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FieldLogicTest {
    @InjectMocks
    private FieldLogic fieldLogic;

    @Mock
    private Board board;

    @Mock
    private ButtonLogic buttonLogic;

    @Test
    void testInitializeField_WhenDark_ThenReturnDarkField() {
        Field field = fieldLogic.initializeField(board, 3, 4);

        assertEquals("e5", field.getCode());
        assertEquals(3 * NUMBER_OF_COLUMNS_AND_ROWS + 4, field.getId());
        assertEquals(4, field.getCoordinates().getX());
        assertEquals(3, field.getCoordinates().getY());
        assertEquals(4 * FIELD_WIDTH, field.getButton().getX());
        assertEquals(3 * FIELD_WIDTH, field.getButton().getY());
        assertEquals(Color.DARK.getAwtColor(), field.getButton().getBackground());
        assertFalse(field.getButton().isEnabled());
    }

    @Test
    void testInitializeField_WhenLight_ThenReturnLightField() {
        Field field = fieldLogic.initializeField(board, 3, 5);

        assertEquals("f5", field.getCode());
        assertEquals(3 * NUMBER_OF_COLUMNS_AND_ROWS + 5, field.getId());
        assertEquals(5, field.getCoordinates().getX());
        assertEquals(3, field.getCoordinates().getY());
        assertEquals(5 * FIELD_WIDTH, field.getButton().getX());
        assertEquals(3 * FIELD_WIDTH, field.getButton().getY());
        assertEquals(Color.LIGHT.getAwtColor(), field.getButton().getBackground());
        assertFalse(field.getButton().isEnabled());
    }

    @Test
    void testAddChessPiece() {
        List<Field> grid = new ArrayList<>(Arrays.asList(new Field().setId(0).setCoordinates(new Coordinates(0, 0)),
                new Field().setId(1).setCoordinates(new Coordinates(1, 0)),
                new Field().setId(2).setCoordinates(new Coordinates(2, 0)),
                new Field().setId(3).setCoordinates(new Coordinates(3, 0))));

        Game game = new Game().setGrid(grid);
        Piece bishop = new Bishop();

        when(buttonLogic.initializeButton(game, 2)).thenReturn(new Button(board, grid.get(2)));

        fieldLogic.addChessPiece(game, "c8", bishop, Color.BLACK);

        assertEquals(Color.BLACK, game.getGrid().get(2).getPiece().getColor());
        assertEquals(bishop, game.getGrid().get(2).getPiece());
    }
}