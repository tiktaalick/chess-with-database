package org.mark.chess.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Chessboard;
import org.mark.chess.game.Game;
import org.mark.chess.move.MoveBuilder;
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
    private MoveBuilder moveBuilder;

    @Mock
    private Chessboard chessboard;

    @Test
    void testPerformAiMove_WhenComputer_ThenPerformAiMove() {
        Game game = Game.create(WHITE).setActivePlayer(new Computer(WHITE)).setChessboard(chessboard);

        when(moveBuilder.createAiFrom(game)).thenReturn(moveBuilder);
        when(moveBuilder.enableValidMoves(game)).thenReturn(moveBuilder);
        when(moveBuilder.createAiTo(game)).thenReturn(moveBuilder);
        when(moveBuilder.setPieceTypeSpecificAttributes(game)).thenReturn(moveBuilder);
        when(moveBuilder.moveRookIfCastling(game)).thenReturn(moveBuilder);
        when(moveBuilder.changeTurn(game)).thenReturn(moveBuilder);
        when(moveBuilder.resetFrom()).thenReturn(moveBuilder);
        when(moveBuilder.setKingFieldColors(game)).thenReturn(moveBuilder);
        when(moveBuilder.setKingFieldColors(game)).thenReturn(moveBuilder);

        AiMoveDirector.setGeneralMoveBuilder(moveBuilder);
        aiMoveDirector.performAiMove(game);

        verify(moveBuilder).build();
    }

    @Test
    void testPerformAiMove_WhenHumanPlayer_ThenBuild() {
        Game game = Game.create(WHITE).setActivePlayer(new Human(WHITE));

        AiMoveDirector.setGeneralMoveBuilder(moveBuilder);
        aiMoveDirector.performAiMove(game);

        verify(moveBuilder).build();
    }
}
