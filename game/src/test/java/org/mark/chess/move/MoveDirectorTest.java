package org.mark.chess.move;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MoveDirectorTest {
//
//    @InjectMocks
//    private MoveDirector moveDirector = new MoveDirector();
//
//    @Mock
//    private MoveBuilder moveBuilder;
//
//    @Mock
//    private Move move;
//
//    @Mock
//    private Field field;
//
//    @Mock
//    private Game game;
//
//    @Mock
//    private Chessboard chessboard;
//
//    @Test
//    void testPerformFromMove() {
//        when(moveBuilder.setMove(move)).thenReturn(moveBuilder);
//        when(moveBuilder.setFrom(field)).thenReturn(moveBuilder);
//        when(moveBuilder.enableValidMoves(game)).thenReturn(moveBuilder);
//
//        MoveDirector.setMoveBuilder(moveBuilder);
//        moveDirector.performFromMove(game, move, field);
//
//        verify(moveBuilder).build();
//    }
//
//    @Test
//    void testPerformResetMove() {
//        when(moveBuilder.setMove(move)).thenReturn(moveBuilder);
//        when(moveBuilder.setKingFieldColors(game)).thenReturn(moveBuilder);
//
//        MoveDirector.setMoveBuilder(moveBuilder);
//        moveDirector.performResetMove(game, move);
//
//        verify(moveBuilder).build();
//    }
//
//    @Test
//    void testPerformRookMove() {
//        Move move = new Move(field);
//        when(moveBuilder.setMove(move)).thenReturn(moveBuilder);
//        when(moveBuilder.setTo(chessboard, field)).thenReturn(moveBuilder);
//        when(moveBuilder.resetFrom()).thenReturn(moveBuilder);
//
//        MoveDirector.setRookMoveBuilder(moveBuilder);
//        moveDirector.performRookMove(chessboard, field, field);
//
//        verify(moveBuilder).build();
//    }
//
//    @Test
//    void testPerformToMove() {
//        when(game.getChessboard()).thenReturn(chessboard);
//        when(moveBuilder.setMove(move)).thenReturn(moveBuilder);
//        when(moveBuilder.setTo(chessboard, field)).thenReturn(moveBuilder);
//        when(moveBuilder.setPieceTypeSpecificAttributes(game)).thenReturn(moveBuilder);
//        when(moveBuilder.moveRookIfCastling(game)).thenReturn(moveBuilder);
//        when(moveBuilder.changeTurn(game)).thenReturn(moveBuilder);
//        when(moveBuilder.resetFrom()).thenReturn(moveBuilder);
//        when(moveBuilder.setKingFieldColors(game)).thenReturn(moveBuilder);
//        when(moveBuilder.performAiMove(game)).thenReturn(moveBuilder);
//
//        MoveDirector.setMoveBuilder(moveBuilder);
//        moveDirector.performToMove(game, move, field);
//
//        verify(moveBuilder).build();
//    }
}
