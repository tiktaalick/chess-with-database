package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.Color;
import org.mark.chess.enums.PlayerType;
import org.mark.chess.model.Game;
import org.mark.chess.model.Player;
import org.mark.chess.service.GameService;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.WHITE;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameLogicTest {
    @InjectMocks
    private GameLogic gameLogic;

    @Mock
    private GameService gameService;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private Game game;

    @Test
    void testIinitializeGame() {
        Board board = new Board(gameService, WHITE);

        Game game = gameLogic.initializeGame(board, WHITE);

        assertTrue(game.isInProgress());
        assertEquals(2, game.getPlayers().size());
        assertEquals(2, game.getPlayers().stream().filter(player -> player.getPlayerType() == PlayerType.HUMAN).count());
        assertTrue(game.getPlayers().stream().map(Player::getColor).anyMatch(color -> color == Color.WHITE));
        assertTrue(game.getPlayers().stream().map(Player::getColor).anyMatch(color -> color == Color.BLACK));

        verify(gridLogic).initializeGrid(game, board);
    }
}