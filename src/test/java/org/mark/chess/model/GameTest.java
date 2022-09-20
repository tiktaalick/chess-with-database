package org.mark.chess.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.Application;
import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.ApplicationFactory;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.PAWN;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameTest {
    private static final int LEFT_CLICK  = 1;
    private static final int RIGHT_CLICK = 3;

    @Mock
    private Board board;

    @Mock
    private Application application;

    @Mock
    private Button button;

    @Mock
    private Grid grid;

    @Mock
    private Field field;

    @Mock
    private Move move;

    @Mock
    private Piece piece;

    @Mock
    private PieceType pieceType;

    @Mock
    private List<Field> fields;

    @Spy
    @InjectMocks
    private Game game = new Game(true, WHITE, grid);

    @Test
    void testCreate() {
        Game game = Game.create(board, WHITE);

        assertEquals(WHITE, game.getCurrentPlayerColor());
        assertEquals(WHITE, game.getHumanPlayerColor());
        assertTrue(game.isInProgress());
        assertEquals(64, game.getGrid().getFields().size());
        assertEquals(BLACK, game.getGrid().getFields().get(0).getPiece().getColor());
        assertEquals("rook", game.getGrid().getFields().get(0).getPiece().getPieceType().getName());
    }

    @Test
    void testGetValidMoves() {
        Grid grid = Grid.create(board, WHITE);
        Field field = new Field(PAWN.createPiece(WHITE)).setCode("d2");

        grid.getFields().set(field.getId(), field);

        game.setGrid(grid);

        List<Field> validMoves = game.getValidMoves(field);

        assertEquals(2, validMoves.size());
    }

    @Test
    void testHandleButtonClick_WhenGameNotInProgress_ThenRestartGame() {
        game.setInProgress(false);
        when(game.getGrid()).thenReturn(grid);
        when(grid.getField(button)).thenReturn(field);

        try (MockedStatic<ApplicationFactory> applicationFactory = Mockito.mockStatic(ApplicationFactory.class)) {
            applicationFactory.when(ApplicationFactory::getInstance).thenReturn(application);

            game.handleButtonClick(board, LEFT_CLICK, button);

            verify(board).dispose();
            verify(application).startApplication(BLACK);
        }
    }

    @Test
    void testHandleButtonClick_WhenLeftClickOnFromField_ThenSetFrom() {
        Field field = new Field(PAWN.createPiece(WHITE));

        when(game.getGrid()).thenReturn(grid);
        when(grid.getField(button)).thenReturn(field.setValidMove(true));
        when(move.isFrom(game, field)).thenReturn(true);
        when(move.setFrom(field)).thenReturn(move);

        game.handleButtonClick(board, LEFT_CLICK, button);

        verify(move).setFrom(field);
        verify(move).enableValidMoves(game, field);
    }

    @Test
    void testHandleButtonClick_WhenLeftClickOnToField_ThenSetTo() {
        Field field = new Field(PAWN.createPiece(WHITE));
        List<Field> list = new ArrayList<>();

        when(game.getGrid()).thenReturn(grid);
        when(grid.getField(button)).thenReturn(field.setValidMove(true));
        when(move.isFrom(game, field)).thenReturn(false);
        when(move.getFrom()).thenReturn(field);
        when(move.setTo(grid, field)).thenReturn(move);
        when(move.setPieceTypeSpecificFields(game, field, field)).thenReturn(move);
        when(move.moveRookWhenCastling(game, field, field)).thenReturn(move);
        when(move.changeTurn(game)).thenReturn(move);
        when(game.resetValidMoves()).thenReturn(list);

        game.handleButtonClick(board, LEFT_CLICK, button);

        verify(move).setTo(grid, field);
        verify(move).setPieceTypeSpecificFields(game, field, field);
        verify(move).moveRookWhenCastling(game, field, field);
        verify(move).changeTurn(game);
        verify(move).resetField(field);
        verify(game).setKingFieldColors(list);
    }

    @Test
    void testHandleButtonClick_WhenRightClickOnToField_ThenResetValidMoves() {
        Field field = new Field(PAWN.createPiece(WHITE));
        List<Field> list = new ArrayList<>();

        when(game.getGrid()).thenReturn(grid);
        when(grid.getField(button)).thenReturn(field.setValidMove(true));
        when(game.resetValidMoves()).thenReturn(list);

        game.handleButtonClick(board, RIGHT_CLICK, button);

        verify(game).setKingFieldColors(list);
    }

    @Test
    void testResetValidMoves() {
        game.setGrid(Grid.create(board, WHITE));

        List<Field> validMoves = game.resetValidMoves();

        assertEquals(20, validMoves.size());
        assertFalse(validMoves.get(0).isAttacking());
        assertFalse(validMoves.get(1).isUnderAttack());
        assertFalse(validMoves.get(2).isValidFrom());
        assertFalse(validMoves.get(3).isValidMove());
    }

    @Test
    void testSetGameProgress_WhenCheckMate_ThenGameIsInNotInProgress() {
        game.setGrid(Grid.create(board, WHITE));

        game.setGameProgress(game.getGrid().getKingField().setCheckMate(true));

        assertFalse(game.isInProgress());
    }

    @Test
    void testSetGameProgress_WhenNoCheckMateNorStaleMate_ThenGameIsInProgress() {
        game.setGrid(Grid.create(board, WHITE));

        game.setGameProgress(game.getGrid().getKingField());

        assertTrue(game.isInProgress());
    }

    @Test
    void testSetGameProgress_WhenStaleMate_ThenGameIsInNotInProgress() {
        game.setGrid(Grid.create(board, WHITE));

        game.setGameProgress(game.getGrid().getKingField().setStaleMate(true));

        assertFalse(game.isInProgress());
    }

    @Test
    void testSetKingFieldColors() {
        Grid grid = Grid.create(board, WHITE);
        game.setGrid(grid);
        List<Field> fields = game.getGrid().getFields();

        try (MockedStatic<Grid> gridMockedStatic = Mockito.mockStatic(Grid.class)) {
            game.setKingFieldColors(fields);

            gridMockedStatic.verify(() -> Grid.setKingFieldFlags(game, fields, grid.getKingField()));
            gridMockedStatic.verify(() -> Grid.setKingFieldFlags(game, fields, grid.getOpponentKingField()));
        }
    }
}
