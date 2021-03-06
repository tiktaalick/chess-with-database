package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.Color;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testInitializeButton_WhenCurrentPlayer_ThenEnabled() {
        Field field = new Field().setCoordinates(new Coordinates(3, 4));
        Button button = new Button(board, field);
        Piece piece = new Pawn().setColor(Color.WHITE);

        List<Field> grid = new ArrayList<>();
        grid.add(field.setButton(button).setPiece(piece));

        JButton actual = buttonLogic.initializeButton(field);

        assertTrue(actual.isEnabled());
        assertNull(actual.getText());
        assertNotNull(actual.getIcon());
        assertEquals(75, actual.getWidth());
        assertNotNull(actual.getIcon());
    }

    @Test
    void testInitializeButton_WhenNotCurrentPlayer_ThenNotEnabled() {
        Field field = new Field().setCoordinates(new Coordinates(3, 4));
        Button button = new Button(board, field);
        Piece piece = new Pawn().setColor(Color.WHITE);

        List<Field> grid = new ArrayList<>();
        grid.add(field.setButton(button).setPiece(piece));

        JButton actual = buttonLogic.initializeButton(field);

        assertNull(actual.getText());
        assertNotNull(actual.getIcon());
        assertEquals(75, actual.getWidth());
        assertNotNull(actual.getIcon());
    }
}