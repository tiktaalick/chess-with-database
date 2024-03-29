package org.mark.chess.swing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.game.Game;
import org.mark.chess.piece.Queen;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class ButtonTest {

    @Mock
    private Board board;

    @Mock
    private Game game;

    @InjectMocks
    private Button button = new Button(board, new Field(null));

    @Test
    void testCreateButtonId_WhenHumanIsPlayingWhithBlack_ThenReturnIdFromRevertedOrder() {
        int buttonId = Button.createButtonId(BLACK, 10);

        assertEquals(53, buttonId);
    }

    @Test
    void testCreateButtonId_WhenHumanIsPlayingWhithWhite_ThenReturnNormalId() {
        int buttonId = Button.createButtonId(WHITE, 10);

        assertEquals(10, buttonId);
    }

    @Test
    void testInitialize() {
        Button initializedButton = button.initialize(new Field(new Queen(BLACK)).setId(12));
        assertEquals(12, initializedButton.getId());
        assertNotNull(initializedButton.getIcon());
        assertEquals("black_queen.png", initializedButton.getIconPath());
    }

    @Test
    void testReset() {
        Field field = new Field(new Queen(BLACK)).setCode("d5");

        Button resetButton = button.initialize(field).reset(field);

        assertEquals("d5", resetButton.getText());
        assertNull(resetButton.getIcon());
    }
}
