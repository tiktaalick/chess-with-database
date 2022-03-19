package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.Application;
import org.mark.chess.enums.GameStatus;
import org.mark.chess.factory.ApplicationFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Move;
import org.mark.chess.service.GameService;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BoardLogicTest {
    private static final int LEFT_CLICK = 1;
    private static final int RIGHT_CLICK = 3;

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

    @Mock
    private ApplicationFactory applicationFactory;

    @Test
    public void testInitializeBoard() {
        Board board = new Board(gameService);

        boardLogic.initializeBoard(board);

        assertEquals(414, board.getSize().getWidth());
        assertEquals(435, board.getSize().getHeight());
        assertEquals(753, board.getLocation().getX());
        assertEquals(323, board.getLocation().getY());
        assertTrue(board.isVisible());
        assertFalse(board.isResizable());
    }

    @Test
    public void testHandleButtonClick_WhenClickOnDisabledButton_ThenDoNothing() {
        JButton button = new JButton();
        button.setEnabled(false);

        Game game = new Game().gameStatus(GameStatus.IN_PROGRESS);

        boardLogic.handleButtonClick(game, board, LEFT_CLICK, button);

        verify(board, never()).dispose();
        verify(application, never()).startApplication();
        verify(moveLogic, never()).setFrom(any(Move.class), any(Field.class));
        verify(moveLogic, never()).enableValidMoves(eq(game), any(Field.class));
        verify(moveLogic, never()).setTo(any(Move.class), any(Field.class));
    }

    @Test
    public void testHandleButtonClick_WhenHasWon_ThenRestartGame() {
        JButton button = new JButton();
        button.setEnabled(true);

        Game game = new Game().gameStatus(GameStatus.HAS_WON);

        doReturn(application).when(applicationFactory).getInstance();

        boardLogic.handleButtonClick(game, board, LEFT_CLICK, button);

        verify(board).dispose();
        verify(application).startApplication();
        verify(moveLogic, never()).setFrom(any(Move.class), any(Field.class));
        verify(moveLogic, never()).enableValidMoves(eq(game), any(Field.class));
        verify(moveLogic, never()).setTo(any(Move.class), any(Field.class));
    }

    @Test
    public void testHandleButtonClick_WhenHasLost_ThenRestartGame() {
        JButton button = new JButton();
        button.setEnabled(true);

        Game game = new Game().gameStatus(GameStatus.HAS_LOST);

        doReturn(application).when(applicationFactory).getInstance();

        boardLogic.handleButtonClick(game, board, LEFT_CLICK, button);

        verify(board).dispose();
        verify(application).startApplication();
        verify(moveLogic, never()).setFrom(any(Move.class), any(Field.class));
        verify(moveLogic, never()).enableValidMoves(eq(game), any(Field.class));
        verify(moveLogic, never()).setTo(any(Move.class), any(Field.class));
    }

    @Test
    public void testHandleButtonClick_WhenMoveFrom_ThenSetFromAndEnableValidMoves() {
        Field fieldClick = new Field();

        JButton button = new JButton();
        button.setEnabled(true);

        Game game = new Game().gameStatus(GameStatus.IN_PROGRESS);

        Board board = new Board(gameService).move(new Move().from(new Field().coordinates(new Coordinates(0, 0))));

        doReturn(fieldClick).when(gridLogic).getField(game.grid(), button);
        doReturn(true).when(moveLogic).isFrom(game, fieldClick);

        boardLogic.handleButtonClick(game, board, LEFT_CLICK, button);

        verify(application, never()).startApplication();
        verify(moveLogic).setFrom(board.move(), fieldClick);
        verify(moveLogic).enableValidMoves(game, fieldClick);
        verify(moveLogic, never()).setTo(any(Move.class), eq(fieldClick));
    }

    @Test
    public void testHandleButtonClick_WhenMoveTo_ThenSetToAndMovePiece() {
        Field fieldClick = new Field();

        JButton button = new JButton();
        button.setEnabled(true);

        Game game = new Game().gameStatus(GameStatus.IN_PROGRESS).grid(new ArrayList<>());

        Board board = new Board(gameService).move(new Move().from(new Field().coordinates(new Coordinates(0, 0))));

        doReturn(fieldClick).when(gridLogic).getField(game.grid(), button);
        doReturn(false).when(moveLogic).isFrom(game, fieldClick);

        boardLogic.handleButtonClick(game, board, LEFT_CLICK, button);

        verify(application, never()).startApplication();
        verify(moveLogic, never()).setFrom(any(Move.class), eq(fieldClick));
        verify(moveLogic, never()).enableValidMoves(game, fieldClick);
        verify(moveLogic).setTo(any(Move.class), eq(fieldClick));
        verify(moveLogic).setChessPieceSpecificFields(game, board.move().from(), fieldClick);
        verify(moveLogic).moveRookWhenCastling(game.grid(), board.move().from(), fieldClick);
        verify(moveLogic).changeTurn(game);
        verify(moveLogic).resetValidMoves(game, board.move());
        verify(moveLogic).resetFrom(board.move());
    }

    @Test
    public void testHandleButtonClick_WhenRightClick__________() {
        Field fieldClick = new Field();

        JButton button = new JButton();
        button.setEnabled(true);

        Game game = new Game().gameStatus(GameStatus.IN_PROGRESS).grid(new ArrayList<>());

        Board board = new Board(gameService).move(new Move().from(new Field().coordinates(new Coordinates(0, 0))));

        doReturn(fieldClick).when(gridLogic).getField(game.grid(), button);

        boardLogic.handleButtonClick(game, board, RIGHT_CLICK, button);

        verify(application, never()).startApplication();
        verify(moveLogic, never()).setFrom(any(Move.class), eq(fieldClick));
        verify(moveLogic, never()).enableValidMoves(game, fieldClick);
        verify(moveLogic, never()).setTo(any(Move.class), eq(fieldClick));
        verify(moveLogic, never()).setChessPieceSpecificFields(game, board.move().from(), fieldClick);
        verify(moveLogic, never()).moveRookWhenCastling(game.grid(), board.move().from(), fieldClick);
        verify(moveLogic, never()).changeTurn(game);
        verify(moveLogic).resetValidMoves(game, board.move());
        verify(moveLogic, never()).resetFrom(board.move());
    }
}