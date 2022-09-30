package org.mark.chess.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.Application;
import org.mark.chess.ApplicationRepository;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.move.Move;
import org.mark.chess.move.MoveDirector;
import org.mark.chess.piece.Pawn;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameTest {

    private static final int LEFT_CLICK        = 1;
    private static final int NUMBER_OF_SQUARES = 64;
    private static final int RIGHT_CLICK       = 3;

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
    private MoveDirector moveDirector;

    @Spy
    @InjectMocks
    private Game game = new Game(WHITE, grid);

    @Test
    void testChangeTurn_WhenBlack_ThenWhite() {
        Game game = new Game(WHITE, Grid.create(board, WHITE)).setCurrentPlayerColor(BLACK);

        game.changeTurn();

        assertEquals(WHITE, game.getCurrentPlayerColor());
    }

    @Test
    void testChangeTurn_WhenWhite_ThenBlack() {
        Game game = new Game(WHITE, Grid.create(board, WHITE)).setCurrentPlayerColor(WHITE);

        game.changeTurn();

        assertEquals(BLACK, game.getCurrentPlayerColor());
    }

    @Test
    void testCreate() {
        Game game = Game.create(board, WHITE);

        assertEquals(WHITE, game.getCurrentPlayerColor());
        assertEquals(WHITE, game.getHumanPlayerColor());
        assertTrue(game.isInProgress());
        assertEquals(64, game.getGrid().getFields().size());
        assertEquals(BLACK, game.getGrid().getFields().get(0).getPieceType().getColor());
        assertEquals("rook", game.getGrid().getFields().get(0).getPieceType().getName());
    }

    @Test
    void testEnableValidMoves_When64EnabledMovesAnd2ValidMoves_ThenDisable62Moves() {
        Grid grid = Grid.create(board, WHITE);
        Field from = new Field(new Pawn(WHITE)).initialize(board, 0);

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
        Grid grid = Grid.create(board, WHITE);
        Field field = new Field(new Pawn(WHITE)).setCode("d2");

        grid.getFields().set(field.getId(), field);

        game.setGrid(grid);

        List<Field> validMoves = game.createValidMoves(field);

        assertEquals(2, validMoves.size());
    }

    @Test
    void testHandleButtonClick_WhenGameNotInProgress_ThenRestartGame() {
        game.setInProgress(false);
        when(game.getGrid()).thenReturn(grid);
        when(grid.getField(button)).thenReturn(field);

        try (MockedStatic<ApplicationRepository> applicationFactory = Mockito.mockStatic(ApplicationRepository.class)) {
            applicationFactory.when(ApplicationRepository::getInstance).thenReturn(application);

            game.handleButtonClick(board, LEFT_CLICK, button);

            verify(board).dispose();
            verify(application).startApplication(BLACK);
        }
    }

    @Test
    void testHandleButtonClick_WhenLeftClickOnFromField_ThenSetFrom() {
        Field field = new Field(new Pawn(WHITE));

        when(game.getGrid()).thenReturn(grid);
        when(grid.getField(button)).thenReturn(field.setValidMove(true));
        when(move.isFrom(game, field)).thenReturn(true);

        Game.setMoveDirector(moveDirector);
        game.handleButtonClick(board, LEFT_CLICK, button);

        verify(moveDirector).performFromMove(game, move, field);
    }

    @Test
    void testHandleButtonClick_WhenLeftClickOnToField_ThenSetTo() {
        Field field = new Field(new Pawn(WHITE));

        when(game.getGrid()).thenReturn(grid);
        when(grid.getField(button)).thenReturn(field.setValidMove(true));
        when(move.isFrom(game, field)).thenReturn(false);

        Game.setMoveDirector(moveDirector);
        game.handleButtonClick(board, LEFT_CLICK, button);

        verify(moveDirector).performToMove(game, move, field);
    }

    @Test
    void testHandleButtonClick_WhenRightClickOnToField_ThenResetValidMoves() {
        Field field = new Field(new Pawn(WHITE));

        when(game.getGrid()).thenReturn(grid);
        when(grid.getField(button)).thenReturn(field.setValidMove(true));

        Game.setMoveDirector(moveDirector);
        game.handleButtonClick(board, RIGHT_CLICK, button);

        verify(moveDirector).performResetMove(game, move);
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
