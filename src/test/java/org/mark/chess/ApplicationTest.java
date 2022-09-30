package org.mark.chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.game.GameService;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mark.chess.player.PlayerColor.WHITE;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApplicationTest {

    @InjectMocks
    private Application application;

    @Mock
    private GameService gameService;

    @Mock
    private Board board;

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
        try (MockedStatic<Board> boardMockedStatic = Mockito.mockStatic(Board.class)) {
            boardMockedStatic.when(() -> Board.createBoard(gameService, WHITE)).thenReturn(board);
            assertEquals(board, application.startApplication(WHITE));
        }
    }
}