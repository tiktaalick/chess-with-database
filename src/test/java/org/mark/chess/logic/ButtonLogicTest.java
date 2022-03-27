package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.Color;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Pawn;
import org.mark.chess.service.GameService;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ButtonLogicTest {
    @InjectMocks
    private ButtonLogic buttonLogic;

    @Mock
    private GameService gameService;

    @Test
    void testGetIconPath() {
        assertEquals("src/main/resources/images/white_pawn.png", buttonLogic.getIconPath(new Pawn(), Color.WHITE));
    }

    @Test
    void testGetIconWidth() {
        Board board = new Board(gameService);
        Field field = new Field().coordinates(new Coordinates(0, 0));
        Button button = new Button(board, field);

        assertEquals(40, buttonLogic.getIconWidth(button));
    }
}