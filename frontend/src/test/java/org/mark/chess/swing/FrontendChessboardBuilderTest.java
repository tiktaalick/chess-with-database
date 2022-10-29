package org.mark.chess.swing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.Chessboard;
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
class FrontendChessboardBuilderTest {

    @InjectMocks
    private FrontendChessboardBuilder frontendChessboardBuilder;

    @Mock
    private FrontendChessboard frontendChessboard;

    @Mock
    private Game game;

    @Mock
    private GameService gameService;

    @Mock
    private GridLayout gridLayout;

    @Mock
    private FrontendField frontendField;

    @Mock
    private Field field;

    @Test
    void testCreateButtons() {
        Chessboard chessboard = Chessboard.create();

        when(frontendChessboard.getGame()).thenReturn(game);
        when(frontendChessboard.getFrontendFields()).thenReturn(new ArrayList<>());
        when(game.getChessboard()).thenReturn(chessboard);

        FrontendChessboard result = frontendChessboardBuilder.setBoard(frontendChessboard).createButtons().build();

        assertEquals(64, result.getFrontendFields().size());
        assertEquals(63, result.getFrontendFields().get(63).getId());
        assertEquals("white_rook.png", result.getFrontendFields().get(63).getIconPath());
    }

    @Test
    void testInitialize() {
        try (MockedStatic<FrontendChessboard> gridMockedStatic = Mockito.mockStatic(FrontendChessboard.class)) {
            gridMockedStatic.when(FrontendChessboard::createGridLayout).thenReturn(gridLayout);
            when(frontendChessboard.getGame()).thenReturn(game);
            when(frontendChessboard.getGameService()).thenReturn(gameService);
            when(frontendChessboard.getDimensionWidth()).thenReturn(800);
            when(frontendChessboard.getDimensionHeight()).thenReturn(600);

            frontendChessboardBuilder.setBoard(frontendChessboard).initialize().build();

            verify(frontendChessboard).setSize(828, 870);
            verify(frontendChessboard).setLayout(gridLayout);
            verify(frontendChessboard).setVisible(true);
            verify(frontendChessboard).setResizable(false);
            verify(frontendChessboard).setDimension();
            verify(frontendChessboard).setLocation(anyInt(), anyInt());
            verify(gameService).resetValidMoves(game);
        }
    }

    @Test
    void testUpdateButtons() {
        Chessboard chessboard = Chessboard.create();
        List<FrontendField> frontendFields = Stream.generate(() -> frontendField).limit(64).collect(Collectors.toList());

        when(frontendChessboard.getGame()).thenReturn(game);
        when(game.getChessboard()).thenReturn(chessboard);
        when(frontendChessboard.getFrontendFields()).thenReturn(frontendFields);
        when(frontendField.reset(any(Field.class))).thenReturn(frontendField);
        when(frontendField.initialize(any(Field.class))).thenReturn(frontendField);
        when(frontendField.setId(anyInt())).thenReturn(frontendField);

        FrontendChessboard result = frontendChessboardBuilder.setBoard(frontendChessboard).updateButtons().build();

        verify(frontendField, times(64)).setId(anyInt());
        verify(frontendField, times(32)).reset(any(Field.class));
        verify(frontendField, times(32)).initialize(any(Field.class));
    }
}

