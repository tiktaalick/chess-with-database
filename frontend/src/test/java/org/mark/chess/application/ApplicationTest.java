package org.mark.chess.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.game.GameService;
import org.mark.chess.swing.FrontendChessboard;
import org.mark.chess.swing.FrontendChessboardBuilder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mark.chess.player.PlayerColor.WHITE;
import static org.mockito.Mockito.verify;
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
    void testMain(@Mock Application application) {
        try (MockedStatic<ApplicationRepository> applicationFactoryMockedStatic = Mockito.mockStatic(ApplicationRepository.class)) {
            applicationFactoryMockedStatic.when(ApplicationRepository::getInstance).thenReturn(application);
            Application.main(new String[]{"bla"});
            verify(application).startApplication(WHITE);
        }
    }

    @Test
    void testStartApplication() {
        Application.setBoardBuilder(frontendChessboardBuilder);
        Application.setGameService(gameService);

        when(frontendChessboardBuilder.setBoard(gameService, WHITE)).thenReturn(frontendChessboardBuilder);
        when(frontendChessboardBuilder.createButtons()).thenReturn(frontendChessboardBuilder);
        when(frontendChessboardBuilder.initialize()).thenReturn(frontendChessboardBuilder);
        when(frontendChessboardBuilder.updateButtons()).thenReturn(frontendChessboardBuilder);
        when(frontendChessboardBuilder.build()).thenReturn(frontendChessboard);

        assertEquals(frontendChessboard, application.startApplication(WHITE));
    }
}
