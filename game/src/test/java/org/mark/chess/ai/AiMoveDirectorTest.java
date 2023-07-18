package org.mark.chess.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Chessboard;
import org.mark.chess.game.Game;
import org.mark.chess.player.Computer;
import org.mark.chess.player.Human;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mark.chess.player.PlayerColor.WHITE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiMoveDirectorTest {

    @Spy
    @InjectMocks
    private AiMoveDirector aiMoveDirector = new AiMoveDirector();

    @Mock
    private AiMoveBuilder aiMoveBuilder;

    @Mock
    private Chessboard chessboard;

    @Test
    void testPerformAiMove_WhenComputer_ThenPerformAiMove() {
        Game game = Game.create(WHITE).setActivePlayer(new Computer(WHITE)).setChessboard(chessboard);

        when(aiMoveBuilder.createAiFrom(game)).thenReturn(aiMoveBuilder);
        when(aiMoveBuilder.enableValidMoves(game)).thenReturn(aiMoveBuilder);
        when(aiMoveBuilder.createAiTo(game)).thenReturn(aiMoveBuilder);
        when(aiMoveBuilder.setPieceTypeSpecificAttributes(game)).thenReturn(aiMoveBuilder);
        when(aiMoveBuilder.moveRookIfCastling(game)).thenReturn(aiMoveBuilder);
        when(aiMoveBuilder.changeTurn(game)).thenReturn(aiMoveBuilder);
        when(aiMoveBuilder.resetFrom()).thenReturn(aiMoveBuilder);
        when(aiMoveBuilder.setKingFieldColors(game)).thenReturn(aiMoveBuilder);
        when(aiMoveBuilder.setKingFieldColors(game)).thenReturn(aiMoveBuilder);

        AiMoveDirector.setAiMoveBuilder(aiMoveBuilder);
        aiMoveDirector.performAiMove(game);

        verify(aiMoveBuilder).build();
    }

    @Test
    void testPerformAiMove_WhenHumanPlayer_ThenBuild() {
        Game game = Game.create(WHITE).setActivePlayer(new Human(WHITE));

        AiMoveDirector.setAiMoveBuilder(aiMoveBuilder);
        aiMoveDirector.performAiMove(game);

        verify(aiMoveBuilder).build();
    }
}
