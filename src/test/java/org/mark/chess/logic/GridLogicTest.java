package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.Color;
import org.mark.chess.factory.InitialPieceFactory;
import org.mark.chess.model.Computer;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Human;
import org.mark.chess.model.King;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.service.GameService;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.JButton;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GridLogicTest {
    private static final int TOTAL_NUMBER_OF_SQUARES = 64;

    @InjectMocks
    private GridLogic gridLogic;

    @Mock
    private GameService gameService;

    @Mock
    private FieldLogic fieldLogic;

    @Test
    void testCreateGridLayout() {
        GridLayout gridLayout = GridLogic.createGridLayout();

        assertEquals(8, gridLayout.getColumns());
        assertEquals(8, gridLayout.getRows());
    }

    @Test
    void testGetField_WhenButtonMatches_ThenReturnField() {
        Board board = new Board(gameService, WHITE);

        Field field1 = new Field().setCoordinates(new Coordinates(0, 0)).setId(0);
        Field field2 = new Field().setCoordinates(new Coordinates(1, 1)).setId(1);
        Field field3 = new Field().setCoordinates(new Coordinates(2, 2)).setId(2);
        Field field4 = new Field().setCoordinates(new Coordinates(3, 3)).setId(3);

        JButton button1 = new Button(board, field1);
        JButton button2 = new Button(board, field2);
        JButton button3 = new Button(board, field3);
        JButton button4 = new Button(board, field4);

        Grid grid = new Grid(new ArrayList<>(Arrays.asList(field1.setButton(button1), field2.setButton(button2), field3.setButton(button3), field4)),
                gridLogic);

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

        Grid grid = new Grid(new ArrayList<>(Arrays.asList(field1, field2, field3, field4)), gridLogic);

        assertEquals(field1, gridLogic.getField(grid, coordinates1));
        assertEquals(field2, gridLogic.getField(grid, coordinates2));
        assertEquals(field3, gridLogic.getField(grid, coordinates3));
        assertNull(gridLogic.getField(grid, new Coordinates(4, 4)));
    }

    @Test
    void testGetKingField() {
        Field field1 = new Field().setCoordinates(new Coordinates(0, 0)).setId(0);
        Field field2 = new Field().setCoordinates(new Coordinates(1, 1)).setId(1).setPiece(new King().setColor(BLACK));
        Field field3 = new Field().setCoordinates(new Coordinates(2, 2)).setId(2);
        Field field4 = new Field().setCoordinates(new Coordinates(3, 3)).setId(3).setPiece(new King().setColor(WHITE));

        Grid grid = new Grid(new ArrayList<>(Arrays.asList(field1, field2, field3, field4)), gridLogic);

        assertEquals(field2, gridLogic.getKingField(grid, BLACK));
        assertEquals(field4, gridLogic.getKingField(grid, WHITE));
        assertNull(gridLogic.getKingField(grid, Color.DARK));
    }

    @Test
    void testInitializeGrid() {
        Game game = new Game().setPlayers(Arrays.asList(new Human(), new Computer())).setHumanPlayerColor(WHITE);
        Board board = new Board(gameService, WHITE);

        try (MockedStatic<InitialPieceFactory> initialPieceFactory = Mockito.mockStatic(InitialPieceFactory.class)) {
            initialPieceFactory.when(() -> InitialPieceFactory.getInitialPiece(anyInt())).thenReturn(new Pawn());

            when(fieldLogic.addChessPiece(any(Field.class), any(Piece.class))).thenReturn(new Field());
            when(fieldLogic.initializeField(eq(board), anyInt())).thenReturn(new Field());

            Grid grid = gridLogic.initializeGrid(game, board);

            assertEquals(TOTAL_NUMBER_OF_SQUARES, grid.getFields().size());
            verify(fieldLogic, times(TOTAL_NUMBER_OF_SQUARES)).initializeField(eq(board), anyInt());
        }
    }
}