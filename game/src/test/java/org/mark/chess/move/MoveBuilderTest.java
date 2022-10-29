package org.mark.chess.move;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.Chessboard;
import org.mark.chess.game.Game;
import org.mark.chess.piece.King;
import org.mark.chess.piece.Pawn;
import org.mark.chess.piece.Rook;
import org.mark.chess.piece.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.isvalidmove.KingIsValidCastlingRule;
import org.mark.chess.piece.maybecapturedenpassant.PawnMayBeCapturedEnPassantRulesEngine;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.piece.PieceType.QUEEN;
import static org.mark.chess.piece.PieceType.ROOK;
import static org.mark.chess.player.PlayerColor.WHITE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoveBuilderTest {

    @InjectMocks
    private MoveBuilder moveBuilder;

    @Mock
    private Move move;

    @Mock
    private MoveDirector rookMoveDirector;

    @Mock
    private PawnMayBeCapturedEnPassantRulesEngine pawnMayBeCapturedEnPassantRulesEngine;

    @Mock
    private Game game;

    @Mock
    private Field field;

    @Mock
    private Chessboard chessboard;

    @Test
    void testBuild() {
        Move result = moveBuilder.setMove(move).build();

        assertEquals(move, result);
    }

    @Test
    void testChangeTurn() {
        moveBuilder.changeTurn(game);

        verify(game).changeTurn();
    }

    @Test
    void testEnableValidMoves() {
        moveBuilder.enableValidMoves(game, field);

        verify(game).enableValidMoves(field);
    }

    @Test
    void testMoveRookWhenCastling_WhenCastling_ThenMoveRook() {
        Field kingFrom = new Field(new King(WHITE)).setCode("e1");
        Field kingTo = new Field(null).setCode("c1");

        when(move.getFrom()).thenReturn(kingFrom);
        when(move.getTo()).thenReturn(kingTo);

        Game game = Game.create(WHITE);

        try (MockedStatic<KingIsValidCastlingRule> kingIsValidCastlingRuleMockedStatic = Mockito.mockStatic(KingIsValidCastlingRule.class)) {
            kingIsValidCastlingRuleMockedStatic
                    .when(() -> KingIsValidCastlingRule.isValidCastling(game.getChessboard(),
                            kingFrom,
                            kingTo,
                            kingTo.getCoordinates().getX(),
                            false,
                            true))
                    .thenReturn(true);

            this.moveBuilder.setRookMoveDirector(rookMoveDirector).moveRookIfCastling(game);

            ArgumentCaptor<Field> fromArgumentCaptor = ArgumentCaptor.forClass(Field.class);
            ArgumentCaptor<Field> toArgumentCaptor = ArgumentCaptor.forClass(Field.class);

            verify(rookMoveDirector).performRookMove(any(Chessboard.class), fromArgumentCaptor.capture(), toArgumentCaptor.capture());

            assertEquals(ROOK, fromArgumentCaptor.getValue().getPieceType().getName());
            assertEquals(WHITE, fromArgumentCaptor.getValue().getPieceType().getColor());
            assertEquals("a1", fromArgumentCaptor.getValue().getCode());
            assertEquals("d1", toArgumentCaptor.getValue().getCode());
        }
    }

    @Test
    void testResetFrom() {
        when(move.getFrom()).thenReturn(field);

        moveBuilder.resetFrom();

        verify(move).getFrom();
        verify(field).setPieceType(null);
    }

    @Test
    void testSetChessPieceSpecificFields_WhenKing_ThenSetHasMovedAtLeastOnce() {
        Chessboard chessboard = Chessboard.create();
        Game game = Game.create(WHITE).setChessboard(chessboard);
        Field from = new Field(new King(WHITE));
        Field to = new Field(null);

        when(move.getFrom()).thenReturn(from);
        when(move.getTo()).thenReturn(to);

        moveBuilder.setPieceTypeSpecificAttributes(game);

        assertTrue(((King) from.getPieceType()).isHasMovedAtLeastOnce());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenPawnIsBeingPromoted_ThenPawnIsPromotedToQueen() {
        Chessboard chessboard = Chessboard.create();
        Game game = Game.create(WHITE).setChessboard(chessboard);
        Field from = new Field(new Pawn(WHITE));
        Field to = new Field(null).setCode("e8");

        when(move.getFrom()).thenReturn(from);
        when(move.getTo()).thenReturn(to);

        moveBuilder.setPieceTypeSpecificAttributes(game);

        assertTrue(from.getPieceType().isPawnBeingPromoted());
        assertEquals(QUEEN, to.getPieceType().getName());
        assertEquals(WHITE, to.getPieceType().getColor());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenPawnMayBeCapturedEnPassant_ThenSetMayBeCapturedEnPassant() {
        Chessboard chessboard = Chessboard.create();
        Game game = Game.create(WHITE).setChessboard(chessboard);
        Field from = new Field(new Pawn(WHITE).setPawnMayBeCapturedEnPassantRulesEngine(pawnMayBeCapturedEnPassantRulesEngine));
        Field to = new Field(null);

        when(pawnMayBeCapturedEnPassantRulesEngine.process(any(IsValidMoveParameter.class))).thenReturn(true);
        when(move.getFrom()).thenReturn(from);
        when(move.getTo()).thenReturn(to);

        moveBuilder.setPieceTypeSpecificAttributes(game);

        assertTrue(((Pawn) from.getPieceType()).isMayBeCapturedEnPassant());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenRook_ThenSetHasMovedAtLeastOnce() {
        Chessboard chessboard = Chessboard.create();
        Game game = Game.create(WHITE).setChessboard(chessboard);
        Field from = new Field(new Rook(WHITE));
        Field to = new Field(null);

        when(move.getFrom()).thenReturn(from);
        when(move.getTo()).thenReturn(to);

        moveBuilder.setPieceTypeSpecificAttributes(game);

        assertTrue(((Rook) from.getPieceType()).isHasMovedAtLeastOnce());
    }

    @Test
    void testSetFrom() {
        moveBuilder.setFrom(field);

        verify(move).setFrom(field);
    }

    @Test
    void testSetKingFieldColors() {
        List<Field> validMoves = new ArrayList<>();

        when(game.resetValidMoves()).thenReturn(validMoves);

        moveBuilder.setKingFieldColors(game);

        verify(game).setKingFieldColors(validMoves);
    }

    @Test
    void testSetTo() {
        moveBuilder.setTo(chessboard, field);

        verify(move).setTo(chessboard, field);
    }
}
