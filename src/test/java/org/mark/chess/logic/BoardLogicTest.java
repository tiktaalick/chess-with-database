package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.Application;
import org.mark.chess.factory.ApplicationFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Move;
import org.mark.chess.service.GameService;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.JButton;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardLogicTest {
    private static final double COORDINATE_X = 546.0;
    private static final double COORDINATE_Y = 105.0;
    private static final double HEIGHT       = 870.0;
    private static final int    LEFT_CLICK   = 1;
    private static final int    RIGHT_CLICK  = 3;
    private static final double WIDTH        = 828.0;

    @Spy
    @InjectMocks
    private BoardLogic boardLogic;

    @Mock
    private GameService gameService;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private MoveLogic moveLogic;

    @Mock
    private Game game;

    @Mock
    private Board board;

    @Mock
    private Application application;

    @Test
    void testHandleButtonClick_WhenGameNotInProgress_ThenRestartGame() {
        JButton button = new JButton();

        Game game = new Game().setInProgress(false).setHumanPlayerColor(BLACK);

        try (MockedStatic<ApplicationFactory> applicationFactory = Mockito.mockStatic(ApplicationFactory.class)) {
            applicationFactory.when(ApplicationFactory::getInstance).thenReturn(application);

            when(gridLogic.getField(game.getGrid(), button)).thenReturn(new Field().setValidMove(true));

            boardLogic.handleButtonClick(game, board, LEFT_CLICK, button);

            verify(board).dispose();
            verify(application).startApplication(WHITE);
            verify(moveLogic, never()).setFrom(any(Move.class), any(Field.class));
            verify(moveLogic, never()).enableValidMoves(eq(game), any(Field.class));
            verify(moveLogic, never()).setTo(any(Grid.class), any(Move.class), any(Field.class));
        }
    }

    @Test
    void testHandleButtonClick_WhenMoveFrom_ThenSetFromAndEnableValidMoves() {
        Field fieldClick = new Field().setValidMove(true);

        JButton button = new JButton();

        Game game = new Game().setInProgress(true);

        Board board = new Board(gameService, WHITE).setMove(new Move().setFrom(new Field().setCoordinates(new Coordinates(0, 0))));

        doReturn(fieldClick).when(gridLogic).getField(game.getGrid(), button);
        doReturn(true).when(moveLogic).isFrom(game, fieldClick);

        boardLogic.handleButtonClick(game, board, LEFT_CLICK, button);

        verify(application, never()).startApplication(WHITE);
        verify(moveLogic).setFrom(board.getMove(), fieldClick);
        verify(moveLogic).enableValidMoves(game, fieldClick);
        verify(moveLogic, never()).setTo(any(Grid.class), any(Move.class), eq(fieldClick));
    }

    @Test
    void testHandleButtonClick_WhenMoveTo_ThenSetToAndMovePiece() {
        Field fieldClick = new Field().setValidMove(true);

        JButton button = new JButton();

        Game game = new Game().setInProgress(true).setGrid(new Grid(new ArrayList<>(), gridLogic));

        Board board = new Board(gameService, WHITE).setMove(new Move().setFrom(new Field().setCoordinates(new Coordinates(0, 0))));

        when(gridLogic.getField(game.getGrid(), button)).thenReturn(fieldClick);
        when(moveLogic.isFrom(game, fieldClick)).thenReturn(false);

        boardLogic.handleButtonClick(game, board, LEFT_CLICK, button);

        verify(application, never()).startApplication(WHITE);
        verify(moveLogic, never()).setFrom(any(Move.class), eq(fieldClick));
        verify(moveLogic, never()).enableValidMoves(game, fieldClick);
        verify(moveLogic).setTo(any(Grid.class), any(Move.class), eq(fieldClick));
        verify(moveLogic).setChessPieceSpecificFields(game, board.getMove().getFrom(), fieldClick);
        verify(moveLogic).moveRookWhenCastling(game, board.getMove().getFrom(), fieldClick);
        verify(moveLogic).changeTurn(game);
        verify(moveLogic).resetValidMoves(game, board.getMove());
        verify(moveLogic).resetField(board.getMove().getFrom());
    }

    @Test
    void testHandleButtonClick_WhenNotAValidMove_ThenDoNothing() {
        JButton button = new JButton();

        Game game = new Game().setInProgress(true);

        when(gridLogic.getField(game.getGrid(), button)).thenReturn(new Field().setValidMove(false));

        boardLogic.handleButtonClick(game, board, LEFT_CLICK, button);

        verify(board, never()).dispose();
        verify(application, never()).startApplication(WHITE);
        verify(moveLogic, never()).setFrom(any(Move.class), any(Field.class));
        verify(moveLogic, never()).enableValidMoves(eq(game), any(Field.class));
        verify(moveLogic, never()).setTo(any(Grid.class), any(Move.class), any(Field.class));
    }

    @Test
    void testHandleButtonClick_WhenRightClick_ThenResetValidMoves() {
        Field fieldClick = new Field();

        JButton button = new JButton();

        Game game = new Game().setInProgress(true).setGrid(new Grid(new ArrayList<>(), gridLogic));

        Board board = new Board(gameService, WHITE).setMove(new Move().setFrom(new Field().setCoordinates(new Coordinates(0, 0))));

        doReturn(fieldClick).when(gridLogic).getField(game.getGrid(), button);

        boardLogic.handleButtonClick(game, board, RIGHT_CLICK, button);

        verify(application, never()).startApplication(WHITE);
        verify(moveLogic, never()).setFrom(any(Move.class), eq(fieldClick));
        verify(moveLogic, never()).enableValidMoves(game, fieldClick);
        verify(moveLogic, never()).setTo(any(Grid.class), any(Move.class), eq(fieldClick));
        verify(moveLogic, never()).setChessPieceSpecificFields(game, board.getMove().getFrom(), fieldClick);
        verify(moveLogic, never()).moveRookWhenCastling(game, board.getMove().getFrom(), fieldClick);
        verify(moveLogic, never()).changeTurn(game);
        verify(moveLogic).resetValidMoves(game, board.getMove());
        verify(moveLogic, never()).resetField(board.getMove().getFrom());
    }

    @Test
    void testInitializeBoard() {
        Board board = new Board(gameService, WHITE);

        boardLogic.initializeBoard(game, board);

        assertEquals(WIDTH, board.getSize().getWidth());
        assertEquals(HEIGHT, board.getSize().getHeight());
        assertEquals(COORDINATE_X, board.getLocation().getX());
        assertEquals(COORDINATE_Y, board.getLocation().getY());
        assertTrue(board.isVisible());
        assertFalse(board.isResizable());
    }
}