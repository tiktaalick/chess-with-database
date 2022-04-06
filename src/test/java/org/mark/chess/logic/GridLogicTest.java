package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.Color;
import org.mark.chess.model.Bishop;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.King;
import org.mark.chess.model.Knight;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;
import org.mark.chess.service.GameService;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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

    @Test
    void testInitializeGrid() {
        Board board = new Board(gameService);
        List<Field> grid = gridLogic.initializeGrid(board);

        assertEquals(64, grid.size());
        verify(fieldLogic, times(64)).initializeField(eq(board), anyInt(), anyInt());
        verify(fieldLogic, times(8)).initializeField(eq(board), eq(4), anyInt());
        verify(fieldLogic, times(8)).initializeField(eq(board), anyInt(), eq(5));
    }

    @Test
    void testAddChessPieces() {
        Game game = new Game();

        gridLogic.addChessPieces(game);

        verify(fieldLogic).addChessPiece(game, "a8", new Rook(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "b8", new Knight(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "c8", new Bishop(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "d8", new Queen(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "e8", new King(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "f8", new Bishop(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "g8", new Knight(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "h8", new Rook(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "a7", new Pawn(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "b7", new Pawn(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "c7", new Pawn(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "d7", new Pawn(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "e7", new Pawn(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "f7", new Pawn(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "g7", new Pawn(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "h7", new Pawn(), Color.BLACK);
        verify(fieldLogic).addChessPiece(game, "a2", new Pawn(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "b2", new Pawn(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "c2", new Pawn(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "d2", new Pawn(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "e2", new Pawn(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "f2", new Pawn(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "g2", new Pawn(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "h2", new Pawn(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "a1", new Rook(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "b1", new Knight(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "c1", new Bishop(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "d1", new Queen(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "e1", new King(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "f1", new Bishop(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "g1", new Knight(), Color.WHITE);
        verify(fieldLogic).addChessPiece(game, "h1", new Rook(), Color.WHITE);
    }

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

        Field field1 = new Field().setCoordinates(coordinates1).setId(0);
        Field field2 = new Field().setCoordinates(coordinates2).setId(1);
        Field field3 = new Field().setCoordinates(coordinates3).setId(2);
        Field field4 = new Field().setCoordinates(coordinates4).setId(3);

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
}