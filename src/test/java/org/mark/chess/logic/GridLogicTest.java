package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.Color;
import org.mark.chess.factory.InitialPieceFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.King;
import org.mark.chess.service.GameService;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.JButton;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GridLogicTest {
    @InjectMocks
    private GridLogic gridLogic;

    @Mock
    private GameService gameService;

    @Mock
    private FieldLogic fieldLogic;

    @Mock
    private InitialPieceFactory initialPieceFactory;

    @Test
    void testCreateGridLayout() {
        GridLayout gridLayout = gridLogic.createGridLayout();

        assertEquals(8, gridLayout.getColumns());
        assertEquals(8, gridLayout.getRows());
    }

    @Test
    void testGetField_WhenButtonMatches_ThenReturnField() {
        Board board = new Board(gameService);

        Field field1 = new Field().setCoordinates(new Coordinates(0, 0)).setId(0);
        Field field2 = new Field().setCoordinates(new Coordinates(1, 1)).setId(1);
        Field field3 = new Field().setCoordinates(new Coordinates(2, 2)).setId(2);
        Field field4 = new Field().setCoordinates(new Coordinates(3, 3)).setId(3);

        JButton button1 = new Button(board, field1);
        JButton button2 = new Button(board, field2);
        JButton button3 = new Button(board, field3);
        JButton button4 = new Button(board, field4);

        List<Field> grid = new ArrayList<>(Arrays.asList(field1.setButton(button1), field2.setButton(button2), field3.setButton(button3), field4));

        assertEquals(field1, gridLogic.getField(grid, button1));
        assertEquals(field2, gridLogic.getField(grid, button2));
        assertEquals(field3, gridLogic.getField(grid, button3));
        assertNull(gridLogic.getField(grid, button4));
    }

    @Test
    void testGetField_WhenCoordinatesMatch_ThenReturnField() {
        Coordinates coordinates1 = new Coordinates(0, 0);
        Coordinates coordinates2 = new Coordinates(1, 1);
        Coordinates coordinates3 = new Coordinates(2, 2);
        Coordinates coordinates4 = new Coordinates(3, 3);

        Field field1 = new Field().setCoordinates(coordinates1);
        Field field2 = new Field().setCoordinates(coordinates2);
        Field field3 = new Field().setCoordinates(coordinates3);
        Field field4 = new Field().setCoordinates(coordinates4);

        List<Field> grid = new ArrayList<>(Arrays.asList(field1, field2, field3, field4));

        assertEquals(field1, gridLogic.getField(grid, coordinates1));
        assertEquals(field2, gridLogic.getField(grid, coordinates2));
        assertEquals(field3, gridLogic.getField(grid, coordinates3));
        assertNull(gridLogic.getField(grid, new Coordinates(4, 4)));
    }

    @Test
    void testGetKingField() {
        Field field1 = new Field().setCoordinates(new Coordinates(0, 0)).setId(0);
        Field field2 = new Field().setCoordinates(new Coordinates(1, 1)).setId(1).setPiece(new King().setColor(Color.BLACK));
        Field field3 = new Field().setCoordinates(new Coordinates(2, 2)).setId(2);
        Field field4 = new Field().setCoordinates(new Coordinates(3, 3)).setId(3).setPiece(new King().setColor(Color.WHITE));

        List<Field> grid = new ArrayList<>(Arrays.asList(field1, field2, field3, field4));

        assertEquals(field2, gridLogic.getKingField(grid, Color.BLACK));
        assertEquals(field4, gridLogic.getKingField(grid, Color.WHITE));
        assertNull(gridLogic.getKingField(grid, Color.DARK));
    }

    @Test
    void testInitializeGrid() {
        Game game = new Game();
        Board board = new Board(gameService);
        List<Field> grid = gridLogic.initializeGrid(game, board);

        assertEquals(64, grid.size());
        verify(fieldLogic, times(64)).initializeField(eq(board), anyInt());
    }
}