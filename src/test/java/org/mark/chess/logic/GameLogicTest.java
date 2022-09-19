package org.mark.chess.logic;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameLogicTest {
//    @InjectMocks
//    private GameLogic gameLogic;
//
//    @Mock
//    private Board board;
//
//    @Mock
//    private Grid grid;
//
//    @Test
//    void testIinitializeGame() {
//        Game game = Game.create(board, WHITE);
//
//        assertTrue(game.isInProgress());
//        assertEquals(2, game.getPlayers().size());
//        assertEquals(2L, game.getPlayers().stream().filter(player -> PlayerType.HUMAN == player.getPlayerType()).count());
//        assertTrue(game.getPlayers().stream().map(Player::getColor).anyMatch(color -> WHITE == color));
//        assertTrue(game.getPlayers().stream().map(Player::getColor).anyMatch(color -> BLACK == color));
//
////        verify(game).initializeGrid(game, board);
//    }
//
//    @Test
//    void testSetGameProgress_WhenKingInCheckMateAndGameInProgress_ThenGameNotInProgress() {
//        Game game = Game.create(board, WHITE);
//        Field kingField = new Field(new King(BLACK)).setCheckMate(true);
//
//        game.setGameProgress(kingField);
//
//        assertFalse(game.isInProgress());
//    }
//
//    @Test
//    void testSetGameProgress_WhenKingInStaleMateAndGameInProgress_ThenGameNotInProgress() {
//        Game game = Game.create(board, WHITE);
//        Field kingField = new Field(new King(BLACK)).setStaleMate(true);
//
//        game.setGameProgress(kingField);
//
//        assertFalse(game.isInProgress());
//    }
//
//    @Test
//    void testSetGameProgress_WhenKingNotInCheckMateNorInStaleMateAndGameInProgress_ThenGameStillInProgress() {
//        Game game = Game.create(board, WHITE);
//        Field kingField = new Field(new King(BLACK));
//
//        game.setGameProgress(kingField);
//
//        assertTrue(game.isInProgress());
//    }
//
//    @Test
//    void testSetGameProgress_WhenKingNotInCheckMateNorInStaleMateAndGameNotInProgress_ThenGameStillNotInProgress() {
//        Game game = Game.create(board, WHITE).setInProgress(false);
//        Field kingField = new Field(new King(BLACK));
//
//        game.setGameProgress(kingField);
//
//        assertFalse(game.isInProgress());
//    }
}