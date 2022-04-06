package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.Color;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Human;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.service.GameService;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.JButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ButtonLogicTest {
    @InjectMocks
    private ButtonLogic buttonLogic;

    @Mock
    private GameService gameService;

    @Mock
    private Board board;

    @Test
    void testGetIconPath() {
        assertEquals("src/main/resources/images/white_pawn.png", buttonLogic.getIconPath(new Pawn(), Color.WHITE));
    }

    @Test
    void testGetIconWidth() {
        Board board = new Board(gameService);
        Field field = new Field().setCoordinates(new Coordinates(0, 0));
        Button button = new Button(board, field);

        assertEquals(40, buttonLogic.getIconWidth(button));
    }

    @Test
    void testInitializeButton_WhenCurrentPlayer_ThenEnabled() {
        Field field = new Field().setCoordinates(new Coordinates(3, 4));
        Button button = new Button(board, field);
        Piece piece = new Pawn().setColor(Color.WHITE);

        List<Field> grid = new ArrayList<>();
        grid.add(field.setButton(button).setPiece(piece));

        JButton actual = buttonLogic.initializeButton(new Game()
                .setGrid(grid)
                .setCurrentPlayerId(Color.WHITE.ordinal())
                .setPlayers(Arrays.asList(new Human().setColor(Color.WHITE), new Human().setColor(Color.BLACK))), 0);

        assertTrue(actual.isEnabled());
        assertEquals("white pawn", actual.getToolTipText());
        assertNull(actual.getText());
        assertNotNull(actual.getIcon());
        assertEquals(50, actual.getWidth());
        assertNotNull(actual.getIcon());
    }

    @Test
    void testInitializeButton_WhenNotCurrentPlayer_ThenNotEnabled() {
        Field field = new Field().setCoordinates(new Coordinates(3, 4));
        Button button = new Button(board, field);
        Piece piece = new Pawn().setColor(Color.WHITE);

        List<Field> grid = new ArrayList<>();
        grid.add(field.setButton(button).setPiece(piece));

        JButton actual = buttonLogic.initializeButton(new Game()
                .setGrid(grid)
                .setCurrentPlayerId(Color.BLACK.ordinal())
                .setPlayers(Arrays.asList(new Human().setColor(Color.WHITE), new Human().setColor(Color.BLACK))), 0);

        assertFalse(actual.isEnabled());
        assertEquals("white pawn", actual.getToolTipText());
        assertNull(actual.getText());
        assertNotNull(actual.getIcon());
        assertEquals(50, actual.getWidth());
        assertNotNull(actual.getIcon());
    }
}