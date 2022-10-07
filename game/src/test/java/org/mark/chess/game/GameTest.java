package org.mark.chess.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.move.Move;
import org.mark.chess.move.MoveDirector;
import org.mark.chess.piece.Pawn;
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
    private Grid grid;

    @Mock
    private Move move;

    @Mock
    private MoveDirector moveDirector;

    @Spy
    @InjectMocks
    private Game game = new Game(WHITE, grid);

    @Test
    void testChangeTurn_WhenBlack_ThenWhite() {
        Game game = new Game(WHITE, Grid.create()).setCurrentPlayerColor(BLACK);

        game.changeTurn();

        assertEquals(WHITE, game.getCurrentPlayerColor());
    }

    @Test
    void testChangeTurn_WhenWhite_ThenBlack() {
        Game game = new Game(WHITE, Grid.create()).setCurrentPlayerColor(WHITE);

        game.changeTurn();

        assertEquals(BLACK, game.getCurrentPlayerColor());
    }

    @Test
    void testCreate() {
        Game game = Game.create(WHITE);

        assertEquals(WHITE, game.getCurrentPlayerColor());
        assertTrue(game.isInProgress());
        assertEquals(64, game.getGrid().getFields().size());
        assertEquals(BLACK, game.getGrid().getFields().get(0).getPieceType().getColor());
        assertEquals("rook", game.getGrid().getFields().get(0).getPieceType().getName());
    }

    @Test
    void testEnableValidMoves_When64EnabledMovesAnd2ValidMoves_ThenDisable62Moves() {
        Grid grid = Grid.create();
        Field from = new Field(new Pawn(WHITE)).setId(0);

        List<Field> validMovesList = grid
                .getFields()
                .stream()
                .filter(field -> field.getCoordinates().getX() == 4)
                .filter(field -> Arrays.asList(4, 5).contains(field.getCoordinates().getY()))
                .collect(Collectors.toList());
        validMovesList.forEach(field -> field.setValidMove(false));

        when(game.getGrid()).thenReturn(grid);
        when(game.createValidMoves(from)).thenReturn(validMovesList);

        game.enableValidMoves(from);

        assertEquals(NUMBER_OF_SQUARES, game.getGrid().getFields().size());
        assertEquals(2L, game.getGrid().getFields().stream().filter(Field::isValidMove).count());
        assertEquals(validMovesList, game.getGrid().getFields().stream().filter(Field::isValidMove).collect(Collectors.toList()));
    }

    @Test
    void testGetValidMoves() {
        Grid grid = Grid.create();
        Field field = new Field(new Pawn(WHITE)).setCode("d2");

        grid.getFields().set(field.getId(), field);

        game.setGrid(grid);

        List<Field> validMoves = game.createValidMoves(field);

        assertEquals(2, validMoves.size());
    }

    @Test
    void testHandleButtonClick_WhenLeftClickOnFromField_ThenSetFrom() {
        Grid grid = Grid.create();
        grid.getFields().get(50).setValidMove(true);

        when(game.getGrid()).thenReturn(grid);
        when(move.isFrom(eq(game), any(Field.class))).thenReturn(true);

        Game.setMoveDirector(moveDirector);
        game.handleButtonClick(LEFT_CLICK, 50);

        verify(moveDirector).performFromMove(eq(game), eq(move), any(Field.class));
    }

    @Test
    void testHandleButtonClick_WhenLeftClickOnToField_ThenSetTo() {
        Grid grid = Grid.create();
        grid.getFields().get(50).setValidMove(true);

        when(game.getGrid()).thenReturn(grid);
        when(move.isFrom(eq(game), any(Field.class))).thenReturn(false);

        Game.setMoveDirector(moveDirector);
        game.handleButtonClick(LEFT_CLICK, 50);

        verify(moveDirector).performToMove(eq(game), eq(move), any(Field.class));
    }

    @Test
    void testHandleButtonClick_WhenRightClickOnToField_ThenResetValidMoves() {
        Grid grid = Grid.create();

        when(game.getGrid()).thenReturn(grid);

        Game.setMoveDirector(moveDirector);
        game.handleButtonClick(RIGHT_CLICK, 63);

        verify(moveDirector).performResetMove(game, move);
    }

    @Test
    void testResetValidMoves() {
        game.setGrid(Grid.create());

        List<Field> validMoves = game.resetValidMoves();

        assertEquals(20, validMoves.size());
        assertFalse(validMoves.get(0).isAttacking());
        assertFalse(validMoves.get(1).isUnderAttack());
        assertFalse(validMoves.get(2).isValidFrom());
        assertFalse(validMoves.get(3).isValidMove());
    }

    @Test
    void testRestart() {
        Game newGame = Game.restart(game);
        assertNotNull(newGame);
        assertNotEquals(game, newGame);
    }

    @Test
    void testSetGameProgress_WhenCheckMate_ThenGameIsInNotInProgress() {
        game.setGrid(Grid.create());

        game.setGameProgress(game.getGrid().getKingField().setCheckMate(true));

        assertFalse(game.isInProgress());
    }

    @Test
    void testSetGameProgress_WhenNoCheckMateNorStaleMate_ThenGameIsInProgress() {
        game.setGrid(Grid.create());

        game.setGameProgress(game.getGrid().getKingField());

        assertTrue(game.isInProgress());
    }

    @Test
    void testSetGameProgress_WhenStaleMate_ThenGameIsInNotInProgress() {
        game.setGrid(Grid.create());

        game.setGameProgress(game.getGrid().getKingField().setStaleMate(true));

        assertFalse(game.isInProgress());
    }

    @Test
    void testSetKingFieldColors() {
        Grid grid = Grid.create();
        game.setGrid(grid);
        List<Field> fields = game.getGrid().getFields();

        try (MockedStatic<Grid> gridMockedStatic = Mockito.mockStatic(Grid.class)) {
            game.setKingFieldColors(fields);

            gridMockedStatic.verify(() -> Grid.setKingFieldFlags(game, fields, grid.getKingField()));
            gridMockedStatic.verify(() -> Grid.setKingFieldFlags(game, fields, grid.getOpponentKingField()));
        }
    }
}
