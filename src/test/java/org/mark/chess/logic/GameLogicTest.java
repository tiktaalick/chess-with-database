package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.PlayerType;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.King;
import org.mark.chess.model.Player;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameLogicTest {
    @InjectMocks
    private GameLogic gameLogic;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private Board board;

    @Test
    void testIinitializeGame() {
        Game game = gameLogic.initializeGame(board, WHITE);

        assertTrue(game.isInProgress());
        assertEquals(2, game.getPlayers().size());
        assertEquals(2L, game.getPlayers().stream().filter(player -> PlayerType.HUMAN == player.getPlayerType()).count());
        assertTrue(game.getPlayers().stream().map(Player::getColor).anyMatch(color -> WHITE == color));
        assertTrue(game.getPlayers().stream().map(Player::getColor).anyMatch(color -> BLACK == color));

        verify(gridLogic).initializeGrid(game, board);
    }

    @Test
    void testSetGameProgress_WhenKingInCheckMateAndGameInProgress_ThenGameNotInProgress() {
        Game game = gameLogic.initializeGame(board, WHITE);
        Field kingField = new Field(new King(BLACK)).setCheckMate(true);

        gameLogic.setGameProgress(game, kingField);

        assertFalse(game.isInProgress());
    }

    @Test
    void testSetGameProgress_WhenKingInStaleMateAndGameInProgress_ThenGameNotInProgress() {
        Game game = gameLogic.initializeGame(board, WHITE);
        Field kingField = new Field(new King(BLACK)).setStaleMate(true);

        gameLogic.setGameProgress(game, kingField);

        assertFalse(game.isInProgress());
    }

    @Test
    void testSetGameProgress_WhenKingNotInCheckMateNorInStaleMateAndGameInProgress_ThenGameStillInProgress() {
        Game game = gameLogic.initializeGame(board, WHITE);
        Field kingField = new Field(new King(BLACK));

        gameLogic.setGameProgress(game, kingField);

        assertTrue(game.isInProgress());
    }

    @Test
    void testSetGameProgress_WhenKingNotInCheckMateNorInStaleMateAndGameNotInProgress_ThenGameStillNotInProgress() {
        Game game = gameLogic.initializeGame(board, WHITE).setInProgress(false);
        Field kingField = new Field(new King(BLACK));

        gameLogic.setGameProgress(game, kingField);

        assertFalse(game.isInProgress());
    }
}