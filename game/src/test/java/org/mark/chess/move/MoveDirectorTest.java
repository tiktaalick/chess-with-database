package org.mark.chess.move;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.game.Game;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoveDirectorTest {

    @InjectMocks
    private MoveDirector moveDirector = new MoveDirector();

    @Mock
    private MoveBuilder moveBuilder;

    @Mock
    private Move move;

    @Mock
    private Field field;

    @Mock
    private Game game;

    @Mock
    private Grid grid;

    @Test
    void testPerformFromMove() {
        when(moveBuilder.setMove(move)).thenReturn(moveBuilder);
        when(moveBuilder.setFrom(field)).thenReturn(moveBuilder);
        when(moveBuilder.enableValidMoves(game, field)).thenReturn(moveBuilder);

        MoveDirector.setGeneralMoveBuilder(moveBuilder);
        moveDirector.performFromMove(game, move, field);

        verify(moveBuilder).build();
    }

    @Test
    void testPerformResetMove() {
        when(moveBuilder.setMove(move)).thenReturn(moveBuilder);
        when(moveBuilder.setKingFieldColors(game)).thenReturn(moveBuilder);

        MoveDirector.setGeneralMoveBuilder(moveBuilder);
        moveDirector.performResetMove(game, move);

        verify(moveBuilder).build();
    }

    @Test
    void testPerformRookMove() {
        Move move = new Move(field);
        when(moveBuilder.setMove(move)).thenReturn(moveBuilder);
        when(moveBuilder.setTo(grid, field)).thenReturn(moveBuilder);
        when(moveBuilder.resetFrom()).thenReturn(moveBuilder);

        MoveDirector.setRookMoveBuilder(moveBuilder);
        moveDirector.performRookMove(grid, field, field);

        verify(moveBuilder).build();
    }

    @Test
    void testPerformToMove() {
        when(game.getGrid()).thenReturn(grid);
        when(moveBuilder.setMove(move)).thenReturn(moveBuilder);
        when(moveBuilder.setTo(grid, field)).thenReturn(moveBuilder);
        when(moveBuilder.setPieceTypeSpecificAttributes(game)).thenReturn(moveBuilder);
        when(moveBuilder.moveRookIfCastling(game)).thenReturn(moveBuilder);
        when(moveBuilder.changeTurn(game)).thenReturn(moveBuilder);
        when(moveBuilder.resetFrom()).thenReturn(moveBuilder);
        when(moveBuilder.setKingFieldColors(game)).thenReturn(moveBuilder);

        MoveDirector.setGeneralMoveBuilder(moveBuilder);
        moveDirector.performToMove(game, move, field);

        verify(moveBuilder).build();
    }
}
