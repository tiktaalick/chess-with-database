package org.mark.chess.swing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.game.Game;
import org.mark.chess.game.GameService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardBuilderTest {

    @InjectMocks
    private BoardBuilder boardBuilder;

    @Mock
    private Board board;

    @Mock
    private Game game;

    @Mock
    private GameService gameService;

    @Mock
    private GridLayout gridLayout;

    @Mock
    private Button button;

    @Mock
    private Field field;

    @Test
    void testCreateButtons() {
        Grid grid = Grid.create();

        when(board.getGame()).thenReturn(game);
        when(board.getButtons()).thenReturn(new ArrayList<>());
        when(game.getGrid()).thenReturn(grid);

        Board result = boardBuilder.setBoard(board).createButtons().build();

        assertEquals(64, result.getButtons().size());
        assertEquals(63, result.getButtons().get(63).getId());
        assertEquals("white_rook.png", result.getButtons().get(63).getIconPath());
    }

    @Test
    void testInitialize() {
        try (MockedStatic<Grid> gridMockedStatic = Mockito.mockStatic(Grid.class)) {
            gridMockedStatic.when(Grid::createGridLayout).thenReturn(gridLayout);
            when(board.getGame()).thenReturn(game);
            when(board.getGameService()).thenReturn(gameService);
            when(board.getDimensionWidth()).thenReturn(800);
            when(board.getDimensionHeight()).thenReturn(600);

            boardBuilder.setBoard(board).initialize().build();

            verify(board).setSize(828, 870);
            verify(board).setLayout(gridLayout);
            verify(board).setVisible(true);
            verify(board).setResizable(false);
            verify(board).setDimension();
            verify(board).setLocation(anyInt(), anyInt());
            verify(gameService).resetValidMoves(game);
        }
    }

    @Test
    void testUpdateButtons() {
        Grid grid = Grid.create();
        List<Button> buttons = Stream.generate(() -> button).limit(64).collect(Collectors.toList());

        when(board.getGame()).thenReturn(game);
        when(game.getGrid()).thenReturn(grid);
        when(board.getButtons()).thenReturn(buttons);
        when(button.reset(any(Field.class))).thenReturn(button);
        when(button.initialize(any(Field.class))).thenReturn(button);
        when(button.setId(anyInt())).thenReturn(button);

        Board result = boardBuilder.setBoard(board).updateButtons().build();

        verify(button, times(64)).setId(anyInt());
        verify(button, times(32)).reset(any(Field.class));
        verify(button, times(32)).initialize(any(Field.class));
    }
}

