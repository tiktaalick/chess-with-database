package org.mark.chess.logic;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardLogicTest {
//    private static final double DIMENSION_HEIGHT    = 600.0;
//    private static final double DIMENSION_WIDTH     = 800.0;
//    private static final int    EXPECTED_LOCATION_X = -14;
//    private static final int    EXPECTED_LOCATION_Y = -135;
//    private static final int    HEIGHT              = 870;
//    private static final int    LEFT_CLICK          = 1;
//    private static final int    RIGHT_CLICK         = 3;
//    private static final int    WIDTH               = 828;
//
//    @Spy
//    @InjectMocks
//    private BoardLogic boardLogic;
//
//    @Mock
//    private Game game;
//
//    @Mock
//    private Board board;
//
//    @Mock
//    private Application application;
//
//    @Mock
//    private GridLayout gridLayout;
//
//    @Mock
//    private Toolkit toolkit;
//
//    @Mock
//    private Button button;
//
//    @Mock
//    private Grid grid;
//
//    @Test
//    void testHandleButtonClick_WhenGameNotInProgress_ThenRestartGame() {
//        Game game = new Game().setInProgress(false).setHumanPlayerColor(BLACK);
//
//        Move move = new Move().setFrom(new Field(null).setCoordinates(new Coordinates(0, 0)));
//
//        try (MockedStatic<ApplicationFactory> applicationFactory = Mockito.mockStatic(ApplicationFactory.class)) {
//            applicationFactory.when(ApplicationFactory::getInstance).thenReturn(application);
//
//            when(grid.getField(button)).thenReturn(new Field(null).setValidMove(true));
//
//            boardLogic.handleButtonClick(game, board, move, LEFT_CLICK, button);
//
//            verify(board).dispose();
//            verify(application).startApplication(WHITE);
//            verify(move, never()).setFrom(any(Move.class), any(Field.class));
//            verify(move, never()).enableValidMoves(eq(game), any(Field.class));
//            verify(move, never()).setTo(any(Grid.class), any(Move.class), any(Field.class));
//        }
//    }

//    @Test
//    void testHandleButtonClick_WhenMoveFrom_ThenSetFromAndEnableValidMoves() {
//        Field fieldClick = new Field(null).setValidMove(true);
//
//        Game game = new Game().setInProgress(true);
//
//        Move move = new Move().setFrom(new Field(null).setCoordinates(new Coordinates(0, 0)));
//
////        doReturn(fieldClick).when(gridLogic).getField(game.getGrid(), button);
//        doReturn(true).when(move).isFrom(game, fieldClick);
//
//        boardLogic.handleButtonClick(game, board, move, LEFT_CLICK, button);
//
//        verify(application, never()).startApplication(WHITE);
//        verify(move).setFrom(move, fieldClick);
//        verify(move).enableValidMoves(game, fieldClick);
//        verify(move, never()).setTo(any(Grid.class), any(Move.class), eq(fieldClick));
//    }

//    @Test
//    void testHandleButtonClick_WhenMoveTo_ThenSetToAndMovePiece() {
//        Field fieldClick = new Field(null).setValidMove(true);
//
//        Game game = new Game().setInProgress(true).setGrid(new Grid(new ArrayList<>(), checkLogic, move));
//
//        Move move = new Move().setFrom(new Field(null).setCoordinates(new Coordinates(0, 0)));
//
////        when(gridLogic.getField(game.getGrid(), button)).thenReturn(fieldClick);
//        when(move.isFrom(game, fieldClick)).thenReturn(false);
//
//        boardLogic.handleButtonClick(game, board, move, LEFT_CLICK, button);
//
//        verify(application, never()).startApplication(WHITE);
//        verify(move, never()).setFrom(any(Move.class), eq(fieldClick));
//        verify(move, never()).enableValidMoves(game, fieldClick);
//        verify(move).setTo(any(Grid.class), any(Move.class), eq(fieldClick));
//        verify(move).setChessPieceSpecificFields(game, move.getFrom(), fieldClick);
//        verify(move).moveRookWhenCastling(game, move.getFrom(), fieldClick);
//        verify(move).changeTurn(game);
//        verify(move).resetValidMoves(game, move);
//        verify(move).resetField(move.getFrom());
//    }

//    @Test
//    void testHandleButtonClick_WhenNotAValidMove_ThenDoNothing() {
//        Game game = new Game().setInProgress(true).setGrid(new Grid(new ArrayList<>(), checkLogic, move));
//
//        when(grid.getField(button)).thenReturn(new Field(null).setValidMove(false));
//
//        boardLogic.handleButtonClick(game, board, new Move(), LEFT_CLICK, button);
//
//        verify(board, never()).dispose();
//        verify(application, never()).startApplication(WHITE);
//        verify(move, never()).setFrom(any(Move.class), any(Field.class));
//        verify(move, never()).enableValidMoves(eq(game), any(Field.class));
//        verify(move, never()).setTo(any(Grid.class), any(Move.class), any(Field.class));
//    }

//    @Test
//    void testHandleButtonClick_WhenRightClick_ThenResetValidMoves() {
//        Field fieldClick = new Field(null);
//
//        Game game = new Game().setInProgress(true).setGrid(new Grid(new ArrayList<>()));
//
//        Move move = new Move().setFrom(new Field(null).setCoordinates(new Coordinates(0, 0)));
//
//        doReturn(fieldClick).when(gridLogic).getField(game.getGrid(), button);

//        game.handleButtonClick(board, move, RIGHT_CLICK, button);
//
//        verify(application, never()).startApplication(WHITE);
//        verify(move, never()).setFrom(any(Move.class), eq(fieldClick));
//        verify(move, never()).enableValidMoves(game, fieldClick);
//        verify(move, never()).setTo(any(Grid.class), any(Move.class), eq(fieldClick));
//        verify(move, never()).setChessPieceSpecificFields(game, move.getFrom(), fieldClick);
//        verify(move, never()).moveRookWhenCastling(game, move.getFrom(), fieldClick);
//        verify(move, never()).changeTurn(game);
//        verify(game).resetValidMoves(move);
//        verify(move, never()).resetField(move.getFrom());
//    }
//
//    @Test
//    void testInitializeBoard() {
//        Move move = new Move().setFrom(new Field(null).setCoordinates(new Coordinates(0, 0)));
//        Dimension dimension = new Dimension();
//        dimension.setSize(DIMENSION_WIDTH, DIMENSION_HEIGHT);
//
//        try (MockedStatic<Grid> gridMockedStatic = Mockito.mockStatic(Grid.class);
//             MockedStatic<Toolkit> toolkitStatic = Mockito.mockStatic(Toolkit.class)) {
//            gridMockedStatic.when(Grid::createGridLayout).thenReturn(gridLayout);
//            toolkitStatic.when(Toolkit::getDefaultToolkit).thenReturn(toolkit);
//            toolkitStatic.when(() -> toolkit.getScreenSize()).thenReturn(dimension);
//
////            board.initialize(game, move);
//
//            verify(board).setSize(WIDTH, HEIGHT);
//            verify(board).setLayout(gridLayout);
//            verify(board).setVisible(Boolean.TRUE);
//            verify(board).setResizable(Boolean.FALSE);
//            verify(board).setLocation(EXPECTED_LOCATION_X, EXPECTED_LOCATION_Y);
//            verify(game).resetValidMoves(move);
//        }
//    }
}