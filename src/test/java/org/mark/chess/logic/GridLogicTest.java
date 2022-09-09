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
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GridLogicTest {
    private static final int MAX_SQUARE_ID           = 63;
    private static final int TOTAL_NUMBER_OF_SQUARES = 64;

    @InjectMocks
    private GridLogic gridLogic;

    @Mock
    private Board board;

    @Mock
    private Button button1;

    @Mock
    private Button button2;

    @Mock
    private Button button3;

    @Mock
    private Button button4;

    @Mock
    private CheckLogic checkLogic;

    @Mock
    private MoveLogic moveLogic;

    @Mock
    private Field field;

    @Test
    void testCalculateGridValue() {
        Field field1 = new Field(new Queen(BLACK)).setCoordinates(new Coordinates(0, 0)).setId(0);
        Field field2 = new Field(new King(BLACK)).setCoordinates(new Coordinates(1, 1)).setId(1);
        Field field3 = new Field(new Rook(WHITE)).setCoordinates(new Coordinates(2, 2)).setId(2);
        Field field4 = new Field(new King(WHITE)).setCoordinates(new Coordinates(3, 3)).setId(3);

        Grid grid = Grid.createGrid(new ArrayList<>(Arrays.asList(field1, field2, field3, field4)), gridLogic);

        assertEquals(-4, gridLogic.calculateGridValue(grid, WHITE));
        assertEquals(4, gridLogic.calculateGridValue(grid, BLACK));
    }

    @Test
    void testCreateGridLayout() {
        GridLayout gridLayout = GridLogic.createGridLayout();

        assertEquals(8, gridLayout.getColumns());
        assertEquals(8, gridLayout.getRows());
    }

    @Test
    void testGetField_WhenButtonMatches_ThenReturnField() {
        Field field1 = new Field(null).setCoordinates(new Coordinates(0, 0)).setId(0);
        Field field2 = new Field(null).setCoordinates(new Coordinates(1, 1)).setId(1);
        Field field3 = new Field(null).setCoordinates(new Coordinates(2, 2)).setId(2);
        Field field4 = new Field(null).setCoordinates(new Coordinates(3, 3)).setId(3);

        Grid grid = Grid.createGrid(new ArrayList<>(Arrays.asList(field1.setButton(button1),
                field2.setButton(button2),
                field3.setButton(button3),
                field4)), gridLogic);

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

        Field field1 = new Field(null).setCoordinates(coordinates1);
        Field field2 = new Field(null).setCoordinates(coordinates2);
        Field field3 = new Field(null).setCoordinates(coordinates3);
        Field field4 = new Field(null).setCoordinates(coordinates4);

        Grid grid = Grid.createGrid(new ArrayList<>(Arrays.asList(field1, field2, field3, field4)), gridLogic);

        assertEquals(field1, gridLogic.getField(grid, coordinates1));
        assertEquals(field2, gridLogic.getField(grid, coordinates2));
        assertEquals(field3, gridLogic.getField(grid, coordinates3));
        assertNull(gridLogic.getField(grid, new Coordinates(4, 4)));
    }

    @Test
    void testGetKingField() {
        Field field1 = new Field(null).setCoordinates(new Coordinates(0, 0)).setId(0);
        Field field2 = new Field(new King(BLACK)).setCoordinates(new Coordinates(1, 1)).setId(1);
        Field field3 = new Field(null).setCoordinates(new Coordinates(2, 2)).setId(2);
        Field field4 = new Field(new King(WHITE)).setCoordinates(new Coordinates(3, 3)).setId(3);

        Grid grid = Grid.createGrid(new ArrayList<>(Arrays.asList(field1, field2, field3, field4)), gridLogic);

        assertEquals(field2, gridLogic.getKingField(grid, BLACK));
        assertEquals(field4, gridLogic.getKingField(grid, WHITE));
        assertNull(gridLogic.getKingField(grid, Color.DARK));
    }

    @Test
    void testInitializeGrid() {
        Game game = new Game().setPlayers(Arrays.asList(new Human(), new Computer())).setHumanPlayerColor(WHITE);

        try (MockedStatic<InitialPieceFactory> initialPieceFactoryMockedStatic = Mockito.mockStatic(InitialPieceFactory.class)) {
            initialPieceFactoryMockedStatic.when(() -> InitialPieceFactory.getInitialPiece(anyInt())).thenReturn(new Pawn(WHITE));

            Grid grid = gridLogic.initializeGrid(game, board);

            assertEquals(TOTAL_NUMBER_OF_SQUARES, grid.getFields().size());
            Field field63 = grid.getFields().stream().filter(field -> field.getId() == MAX_SQUARE_ID).findFirst().orElse(null);
            assertNotNull(field63);
            assertEquals(63, field63.getId());
            assertEquals("h1", field63.getCode());
            assertEquals(8, field63.getCoordinates().getX());
            assertEquals(1, field63.getCoordinates().getY());
            assertNotNull(field63.getButton());
            assertEquals("white", field63.getPiece().getColor().getName());
            assertEquals("pawn", field63.getPiece().getPieceType().getName());
        }
    }

    @Test
    void testSetKingFieldFlags_WhenInCheckAndNotAbleToMove_ThenCheckMate() {
        List<Field> fields = IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> {
            Field field = new Field(null).setId(id);
            return field.setButton(button1);
        }).collect(Collectors.toList());

        Grid grid = Grid.createGrid(fields, gridLogic);
        Game game = new Game().setGrid(grid);

        Field kingField = new Field(new King(BLACK));

        Collection<Field> allValidMoves = List.of(new Field(null).setId(8), new Field(null).setId(9), new Field(null).setId(10));

        when(checkLogic.isInCheckNow(grid, kingField, kingField, false)).thenReturn(true);
        when(moveLogic.isNotAbleToMove(game, kingField, allValidMoves)).thenReturn(true);

        gridLogic.setKingFieldFlags(game, allValidMoves, kingField);

        assertTrue(kingField.isCheckMate());
        assertFalse(kingField.isStaleMate());
    }

    @Test
    void testSetKingFieldFlags_WhenNotInCheckAndNotAbleToMove_ThenStaleMate() {
        List<Field> fields = IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> {
            Field field = new Field(null).setId(id);
            return field.setButton(button1);
        }).collect(Collectors.toList());

        Grid grid = Grid.createGrid(fields, gridLogic);
        Game game = new Game().setGrid(grid);

        Field kingField = new Field(new King(BLACK));

        Collection<Field> allValidMoves = List.of(new Field(null).setId(8), new Field(null).setId(9), new Field(null).setId(10));

        when(checkLogic.isInCheckNow(grid, kingField, kingField, false)).thenReturn(false);
        when(moveLogic.isNotAbleToMove(game, kingField, allValidMoves)).thenReturn(true);

        gridLogic.setKingFieldFlags(game, allValidMoves, kingField);

        assertFalse(kingField.isCheckMate());
        assertTrue(kingField.isStaleMate());
    }
}