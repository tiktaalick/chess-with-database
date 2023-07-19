package org.mark.chess.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Field;
import org.mark.chess.move.Move;
import org.mark.chess.piece.Pawn;
import org.mark.chess.player.Human;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mark.chess.player.PlayerColor.WHITE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    private static final int BUTTON_ID  = 2;
    private static final int LEFT_CLICK = 1;

    @InjectMocks
    private GameService gameService;

    @Mock
    private Game game;

    @Mock
    private Chessboard chessboard;

    @Test
    void testCreateGame() {
        try (MockedStatic<Game> gameMockedStatic = Mockito.mockStatic(Game.class)) {
            gameService.createGame(WHITE);

            gameMockedStatic.verify(() -> Game.create(WHITE));
        }
    }

    @Test
    void testHandleButtonClick_WhenGameInProgress_ThenRestartGame() {
        when(game.isInProgress()).thenReturn(true);

        gameService.handleButtonClick(game, LEFT_CLICK, BUTTON_ID);

        verify(game).handleButtonClick(LEFT_CLICK, BUTTON_ID);
    }

    @Test
    void testHandleButtonClick_WhenGameNotInProgress_ThenRestartGame() {
        when(game.isInProgress()).thenReturn(false);

        try (MockedStatic<Game> gameMockedStatic = Mockito.mockStatic(Game.class)) {
            gameService.handleButtonClick(game, LEFT_CLICK, BUTTON_ID);

            gameMockedStatic.verify(() -> Game.restart(game));
        }
    }

    @Test
    void testResetValidMoves() {
        Move move = new Move(new Field(new Pawn(WHITE)));

        when(game.getChessboard()).thenReturn(chessboard);
        when(game.getMove()).thenReturn(move);
        when(game.getActivePlayer()).thenReturn(new Human(WHITE));

        gameService.resetValidMoves(game);

        verify(game).getChessboard();
        verify(chessboard).resetValidMoves(move, WHITE);
    }
}