package org.mark.chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.game.GameService;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.BoardBuilder;
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
    private Board board;

    @Mock
    private BoardBuilder boardBuilder;

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
        Application.setBoardBuilder(boardBuilder);
        Application.setGameService(gameService);

        when(boardBuilder.setBoard(gameService, WHITE)).thenReturn(boardBuilder);
        when(boardBuilder.createButtons()).thenReturn(boardBuilder);
        when(boardBuilder.initialize()).thenReturn(boardBuilder);
        when(boardBuilder.updateButtons()).thenReturn(boardBuilder);
        when(boardBuilder.build()).thenReturn(board);

        assertEquals(board, application.startApplication(WHITE));
    }
}
