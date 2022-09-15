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
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

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
    private static final double DIMENSION_HEIGHT    = 600.0;
    private static final double DIMENSION_WIDTH     = 800.0;
    private static final int    EXPECTED_LOCATION_X = -14;
    private static final int    EXPECTED_LOCATION_Y = -135;
    private static final int    HEIGHT              = 870;
    private static final int    LEFT_CLICK          = 1;
    private static final int    RIGHT_CLICK         = 3;
    private static final int    WIDTH               = 828;

    @Spy
    @InjectMocks
    private BoardLogic boardLogic;

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
    private GridLayout gridLayout;

    @Mock
    private Toolkit toolkit;

    @Mock
    private Button button;

    @Mock
    private ColorLogic colorLogic;

    @Test
    void testHandleButtonClick_WhenGameNotInProgress_ThenRestartGame() {
        Game game = new Game().setInProgress(false).setHumanPlayerColor(BLACK);

        Move move = new Move().setFrom(new Field(null).setCoordinates(new Coordinates(0, 0)));

        try (MockedStatic<ApplicationFactory> applicationFactory = Mockito.mockStatic(ApplicationFactory.class)) {
            applicationFactory.when(ApplicationFactory::getInstance).thenReturn(application);

            when(gridLogic.getField(game.getGrid(), button)).thenReturn(new Field(null).setValidMove(true));

            boardLogic.handleButtonClick(game, board, move, LEFT_CLICK, button);

            verify(board).dispose();
            verify(application).startApplication(WHITE);
            verify(moveLogic, never()).setFrom(any(Move.class), any(Field.class));
            verify(moveLogic, never()).enableValidMoves(eq(game), any(Field.class));
            verify(moveLogic, never()).setTo(any(Grid.class), any(Move.class), any(Field.class));
        }
    }

    @Test
    void testHandleButtonClick_WhenMoveFrom_ThenSetFromAndEnableValidMoves() {
        Field fieldClick = new Field(null).setValidMove(true);

        Game game = new Game().setInProgress(true);

        Move move = new Move().setFrom(new Field(null).setCoordinates(new Coordinates(0, 0)));

        doReturn(fieldClick).when(gridLogic).getField(game.getGrid(), button);
        doReturn(true).when(moveLogic).isFrom(game, fieldClick);

        boardLogic.handleButtonClick(game, board, move, LEFT_CLICK, button);

        verify(application, never()).startApplication(WHITE);
        verify(moveLogic).setFrom(move, fieldClick);
        verify(moveLogic).enableValidMoves(game, fieldClick);
        verify(moveLogic, never()).setTo(any(Grid.class), any(Move.class), eq(fieldClick));
    }

    @Test
    void testHandleButtonClick_WhenMoveTo_ThenSetToAndMovePiece() {
        Field fieldClick = new Field(null).setValidMove(true);

        Game game = new Game().setInProgress(true).setGrid(Grid.createGrid(new ArrayList<>(), gridLogic));

        Move move = new Move().setFrom(new Field(null).setCoordinates(new Coordinates(0, 0)));

        when(gridLogic.getField(game.getGrid(), button)).thenReturn(fieldClick);
        when(moveLogic.isFrom(game, fieldClick)).thenReturn(false);

        boardLogic.handleButtonClick(game, board, move, LEFT_CLICK, button);

        verify(application, never()).startApplication(WHITE);
        verify(moveLogic, never()).setFrom(any(Move.class), eq(fieldClick));
        verify(moveLogic, never()).enableValidMoves(game, fieldClick);
        verify(moveLogic).setTo(any(Grid.class), any(Move.class), eq(fieldClick));
        verify(moveLogic).setChessPieceSpecificFields(game, move.getFrom(), fieldClick);
        verify(moveLogic).moveRookWhenCastling(game, move.getFrom(), fieldClick);
        verify(moveLogic).changeTurn(game);
        verify(moveLogic).resetValidMoves(game, move);
        verify(moveLogic).resetField(move.getFrom());
    }

    @Test
    void testHandleButtonClick_WhenNotAValidMove_ThenDoNothing() {
        Game game = new Game().setInProgress(true);

        when(gridLogic.getField(game.getGrid(), button)).thenReturn(new Field(null).setValidMove(false));

        boardLogic.handleButtonClick(game, board, new Move(), LEFT_CLICK, button);

        verify(board, never()).dispose();
        verify(application, never()).startApplication(WHITE);
        verify(moveLogic, never()).setFrom(any(Move.class), any(Field.class));
        verify(moveLogic, never()).enableValidMoves(eq(game), any(Field.class));
        verify(moveLogic, never()).setTo(any(Grid.class), any(Move.class), any(Field.class));
    }

    @Test
    void testHandleButtonClick_WhenRightClick_ThenResetValidMoves() {
        Field fieldClick = new Field(null);

        Game game = new Game().setInProgress(true).setGrid(Grid.createGrid(new ArrayList<>(), gridLogic));

        Move move = new Move().setFrom(new Field(null).setCoordinates(new Coordinates(0, 0)));

        doReturn(fieldClick).when(gridLogic).getField(game.getGrid(), button);

        boardLogic.handleButtonClick(game, board, move, RIGHT_CLICK, button);

        verify(application, never()).startApplication(WHITE);
        verify(moveLogic, never()).setFrom(any(Move.class), eq(fieldClick));
        verify(moveLogic, never()).enableValidMoves(game, fieldClick);
        verify(moveLogic, never()).setTo(any(Grid.class), any(Move.class), eq(fieldClick));
        verify(moveLogic, never()).setChessPieceSpecificFields(game, move.getFrom(), fieldClick);
        verify(moveLogic, never()).moveRookWhenCastling(game, move.getFrom(), fieldClick);
        verify(moveLogic, never()).changeTurn(game);
        verify(moveLogic).resetValidMoves(game, move);
        verify(moveLogic, never()).resetField(move.getFrom());
    }

    @Test
    void testInitializeBoard() {
        Move move = new Move().setFrom(new Field(null).setCoordinates(new Coordinates(0, 0)));
        Dimension dimension = new Dimension();
        dimension.setSize(DIMENSION_WIDTH, DIMENSION_HEIGHT);

        try (MockedStatic<GridLogic> gridLogic = Mockito.mockStatic(GridLogic.class);
             MockedStatic<Toolkit> toolkitStatic = Mockito.mockStatic(Toolkit.class)) {
            gridLogic.when(GridLogic::createGridLayout).thenReturn(gridLayout);
            toolkitStatic.when(Toolkit::getDefaultToolkit).thenReturn(toolkit);
            toolkitStatic.when(() -> toolkit.getScreenSize()).thenReturn(dimension);

            boardLogic.initializeBoard(game, board, move);

            verify(board).setSize(WIDTH, HEIGHT);
            verify(board).setLayout(gridLayout);
            verify(board).setVisible(Boolean.TRUE);
            verify(board).setResizable(Boolean.FALSE);
            verify(board).setLocation(EXPECTED_LOCATION_X, EXPECTED_LOCATION_Y);
            verify(moveLogic).resetValidMoves(game, move);
        }
    }
}