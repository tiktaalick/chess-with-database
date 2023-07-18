package org.mark.chess.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.game.GameService;
import org.mark.chess.swing.FrontendChessboard;
import org.mark.chess.swing.FrontendChessboardBuilder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mark.chess.player.PlayerColor.WHITE;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationTest {

    @InjectMocks
    private Application application;

    @Mock
    private GameService gameService;

    @Mock
    private FrontendChessboard frontendChessboard;

    @Mock
    private FrontendChessboardBuilder frontendChessboardBuilder;

    @Test
    void testStartApplication() {
        Application.setBoardBuilder(frontendChessboardBuilder);
        Application.setGameService(gameService);

        when(frontendChessboardBuilder.setBoard(gameService, WHITE)).thenReturn(frontendChessboardBuilder);
        when(frontendChessboardBuilder.createFields()).thenReturn(frontendChessboardBuilder);
        when(frontendChessboardBuilder.initialize()).thenReturn(frontendChessboardBuilder);
        when(frontendChessboardBuilder.updateFields()).thenReturn(frontendChessboardBuilder);
        when(frontendChessboardBuilder.build()).thenReturn(frontendChessboard);

        assertEquals(frontendChessboard, application.startApplication(WHITE));
    }
}
