package org.mark.chess.logic;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.Color;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Human;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.swing.Button.FIELD_WIDTH;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Data
@ExtendWith(MockitoExtension.class)
class FieldLogicTest {
    private static final int NUMBER_OF_COLUMNS = 8;

    @InjectMocks
    private FieldLogic fieldLogic;

    @Mock
    private Board board;

    @Mock
    private Game game;

    @Mock
    private ButtonLogic buttonLogic;

    @Test
    void testInitializeField_WhenDark_ThenReturnDarkField() {
        Field field = fieldLogic.initializeField(board, 3, 4, NUMBER_OF_COLUMNS);

        assertEquals(3 * NUMBER_OF_COLUMNS + 4, field.id());
        assertEquals(4, field.coordinates().x());
        assertEquals(3, field.coordinates().y());
        assertEquals(4 * FIELD_WIDTH, field.button().getX());
        assertEquals(3 * FIELD_WIDTH, field.button().getY());
        assertEquals(Color.DARK.getAwtColor(), field.button().getBackground());
        assertFalse(field.button().isEnabled());
    }

    @Test
    void testInitializeField_WhenLight_ThenReturnLightField() {
        Field field = fieldLogic.initializeField(board, 3, 5, NUMBER_OF_COLUMNS);

        assertEquals(3 * NUMBER_OF_COLUMNS + 5, field.id());
        assertEquals(5, field.coordinates().x());
        assertEquals(3, field.coordinates().y());
        assertEquals(5 * FIELD_WIDTH, field.button().getX());
        assertEquals(3 * FIELD_WIDTH, field.button().getY());
        assertEquals(Color.LIGHT.getAwtColor(), field.button().getBackground());
        assertFalse(field.button().isEnabled());
    }

    @Test
    void testInitializeButton_WhenCurrentPlayer_ThenEnabled() {
        Button button = new Button(3, 4, Color.DARK.getAwtColor(), "", board, board);
        Piece piece = new Pawn().color(Color.WHITE);

        List<Field> grid = new ArrayList<>();
        grid.add(new Field().button(button).piece(piece));

        when(buttonLogic.getIconWidth(button)).thenReturn(50);

        JButton actual = fieldLogic.initializeButton(new Game().grid(grid)
                .currentPlayerIndex(Color.WHITE.ordinal())
                .players(Arrays.asList(new Human().color(Color.WHITE), new Human().color(Color.BLACK))), 0);

        assertTrue(actual.isEnabled());
        assertEquals("white pawn", actual.getToolTipText());
        assertNull(actual.getText());
        assertNotNull(actual.getIcon());

        verify(buttonLogic, times(2)).getIconWidth(button);
        verify(buttonLogic).getIconPath(piece, piece.color());
    }

    @Test
    void testInitializeButton_WhenNotCurrentPlayer_ThenNotEnabled() {
        Button button = new Button(3, 4, Color.DARK.getAwtColor(), "", board, board);
        Piece piece = new Pawn().color(Color.WHITE);

        List<Field> grid = new ArrayList<>();
        grid.add(new Field().button(button).piece(piece));

        when(buttonLogic.getIconWidth(button)).thenReturn(50);

        JButton actual = fieldLogic.initializeButton(new Game().grid(grid)
                .currentPlayerIndex(Color.BLACK.ordinal())
                .players(Arrays.asList(new Human().color(Color.WHITE), new Human().color(Color.BLACK))), 0);

        assertFalse(actual.isEnabled());
        assertEquals("white pawn", actual.getToolTipText());
        assertNull(actual.getText());
        assertNotNull(actual.getIcon());

        verify(buttonLogic, times(2)).getIconWidth(button);
        verify(buttonLogic).getIconPath(piece, piece.color());
    }
}