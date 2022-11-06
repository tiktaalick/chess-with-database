package org.mark.chess.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Field;
import org.mark.chess.move.Move;
import org.mark.chess.move.MoveDirector;
import org.mark.chess.piece.Pawn;
import org.mark.chess.player.Human;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameTest {

    private static final int LEFT_CLICK        = 1;
    private static final int NUMBER_OF_SQUARES = 64;
    private static final int RIGHT_CLICK       = 3;

    @Mock
    private Chessboard chessboard;

    @Mock
    private Move move;

    @Mock
    private MoveDirector moveDirector;

    @Spy
    @InjectMocks
    private Game game = new Game(WHITE, chessboard);

    @Test
    void testChangeTurn_WhenBlack_ThenWhite() {
        Game game = new Game(WHITE, Chessboard.create()).setActivePlayer(new Human(BLACK));

        game.changeTurn();

        assertEquals(WHITE, game.getActivePlayer().getColor());
    }

    @Test
    void testChangeTurn_WhenWhite_ThenBlack() {
        Game game = new Game(WHITE, Chessboard.create()).setActivePlayer(new Human(WHITE));

        game.changeTurn();

        assertEquals(BLACK, game.getActivePlayer().getColor());
    }

    @Test
    void testCreate() {
        Game game = Game.create(WHITE);

        assertEquals(WHITE, game.getActivePlayer().getColor());
        assertTrue(game.isInProgress());
        assertEquals(64, game.getChessboard().getFields().size());
        assertEquals(BLACK, game.getChessboard().getFields().get(0).getPieceType().getColor());
        assertEquals("rook", game.getChessboard().getFields().get(0).getPieceType().getName());
    }

    @Test
    void testEnableValidMoves_When64EnabledMovesAnd2ValidMoves_ThenDisable62Moves() {
        Chessboard chessboard = Chessboard.create();
        Field from = new Field(new Pawn(WHITE)).setId(0);

        List<Field> validMovesList = chessboard
                .getFields()
                .stream()
                .filter(field -> field.getCoordinates().getX() == 4)
                .filter(field -> Arrays.asList(4, 5).contains(field.getCoordinates().getY()))
                .collect(Collectors.toList());
        validMovesList.forEach(field -> field.setValidTo(false));

        when(game.getChessboard()).thenReturn(chessboard);
        when(game.createValidToFields(from)).thenReturn(validMovesList);

        game.enableValidMoves(from);

        assertEquals(NUMBER_OF_SQUARES, game.getChessboard().getFields().size());
        assertEquals(2L, game.getChessboard().getFields().stream().filter(Field::hasValidTo).count());
        assertEquals(validMovesList, game.getChessboard().getFields().stream().filter(Field::hasValidTo).collect(Collectors.toList()));
    }

    @Test
    void testGetValidMoves() {
        Chessboard chessboard = Chessboard.create();
        Field field = new Field(new Pawn(WHITE)).setCode("d2");

        chessboard.getFields().set(field.getId(), field);

        game.setChessboard(chessboard);

        List<Field> validMoves = game.createValidToFields(field);

        assertEquals(2, validMoves.size());
    }

    @Test
    void testHandleButtonClick_WhenLeftClickOnFromField_ThenSetFrom() {
        Chessboard chessboard = Chessboard.create();
        chessboard.getFields().get(50).setValidTo(true);

        when(game.getChessboard()).thenReturn(chessboard);
        when(move.isFrom(eq(game), any(Field.class))).thenReturn(true);

        Game.setMoveDirector(moveDirector);
        game.handleButtonClick(LEFT_CLICK, 50);

        verify(moveDirector).performFromMove(eq(game), eq(move), any(Field.class));
    }

    @Test
    void testHandleButtonClick_WhenLeftClickOnToField_ThenSetTo() {
        Chessboard chessboard = Chessboard.create();
        chessboard.getFields().get(50).setValidTo(true);

        when(game.getChessboard()).thenReturn(chessboard);
        when(move.isFrom(eq(game), any(Field.class))).thenReturn(false);

        Game.setMoveDirector(moveDirector);
        game.handleButtonClick(LEFT_CLICK, 50);

        verify(moveDirector).performToMove(eq(game), eq(move), any(Field.class));
    }

    @Test
    void testHandleButtonClick_WhenRightClickOnToField_ThenResetValidMoves() {
        Chessboard chessboard = Chessboard.create();

        when(game.getChessboard()).thenReturn(chessboard);

        Game.setMoveDirector(moveDirector);
        game.handleButtonClick(RIGHT_CLICK, 63);

        verify(moveDirector).performResetMove(game, move);
    }

    @Test
    void testResetValidMoves() {
        game.setChessboard(Chessboard.create());

        List<Field> validMoves = game.resetValidMoves();

        assertEquals(20, validMoves.size());
        assertFalse(validMoves.get(0).isAttacking());
        assertFalse(validMoves.get(1).isUnderAttack());
        assertFalse(validMoves.get(2).isValidFrom());
        assertFalse(validMoves.get(3).hasValidTo());
    }

    @Test
    void testRestart() {
        Game newGame = Game.restart(game);
        assertNotNull(newGame);
        assertNotEquals(game, newGame);
    }

    @Test
    void testSetGameProgress_WhenCheckMate_ThenGameIsInNotInProgress() {
        game.setChessboard(Chessboard.create());

        game.setGameProgress(game.getChessboard().getKingField().setCheckMate(true));

        assertFalse(game.isInProgress());
    }

    @Test
    void testSetGameProgress_WhenNoCheckMateNorStaleMate_ThenGameIsInProgress() {
        game.setChessboard(Chessboard.create());

        game.setGameProgress(game.getChessboard().getKingField());

        assertTrue(game.isInProgress());
    }

    @Test
    void testSetGameProgress_WhenStaleMate_ThenGameIsInNotInProgress() {
        game.setChessboard(Chessboard.create());

        game.setGameProgress(game.getChessboard().getKingField().setStaleMate(true));

        assertFalse(game.isInProgress());
    }

    @Test
    void testSetKingFieldColors() {
        Chessboard chessboard = Chessboard.create();
        game.setChessboard(chessboard);
        List<Field> fields = game.getChessboard().getFields();

        try (MockedStatic<Chessboard> gridMockedStatic = Mockito.mockStatic(Chessboard.class)) {
            game.setKingFieldColors(fields);

            gridMockedStatic.verify(() -> Chessboard.setKingFieldFlags(game, fields, chessboard.getKingField()));
            gridMockedStatic.verify(() -> Chessboard.setKingFieldFlags(game, fields, chessboard.getOpponentKingField()));
        }
    }
}
