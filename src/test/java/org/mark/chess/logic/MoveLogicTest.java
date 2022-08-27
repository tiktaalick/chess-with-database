package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.PieceFactory;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Human;
import org.mark.chess.model.King;
import org.mark.chess.model.Move;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;
import org.mark.chess.service.GameService;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoveLogicTest {
    private static final long ALL_BUT_TWO_SQUARES         = 62L;
    private static final int  LAST_SQUARE_ON_THE_BOARD_ID = 63;
    private static final int  NUMBER_OF_SQUARES           = 64;

    @InjectMocks
    private MoveLogic moveLogic;

    @Mock
    private FieldLogic fieldLogic;

    @Mock
    private GameService gameService;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private KingLogic kingLogic;

    @Mock
    private PawnLogic pawnLogic;

    @Mock
    private PieceFactory pieceFactory;

    @Mock
    private PieceLogicFactory pieceLogicFactory;

    @Test
    void testChangeTurn_WhenBlack_ThenWhite() {
        Game game = new Game().setCurrentPlayerColor(BLACK);
        moveLogic.changeTurn(game);

        assertEquals(WHITE, game.getCurrentPlayerColor());
    }

    @Test
    void testChangeTurn_WhenWhite_ThenBlack() {
        Game game = new Game().setCurrentPlayerColor(WHITE);
        moveLogic.changeTurn(game);

        assertEquals(BLACK, game.getCurrentPlayerColor());
    }

    @Test
    void testEnableValidMoves_When64EnabledMovesAnd2ValidMoves_ThenDisable62Moves() {
        Board board = new Board(gameService, WHITE);

        Grid grid = new Grid(IntStream.rangeClosed(0, LAST_SQUARE_ON_THE_BOARD_ID).mapToObj(id -> {
            Field field = new Field().setId(id).setValidMove(false);
            return field.setButton(new Button(board, field));
        }).collect(Collectors.toList()), gridLogic);

        Game game = new Game().setGrid(grid);
        Field from = new Field().setPiece(new Pawn().setColor(WHITE));

        List<Field> validMovesList = grid
                .getFields()
                .stream()
                .filter(field -> field.getCoordinates().getX() == 4)
                .filter(field -> Arrays.asList(4, 5).contains(field.getCoordinates().getY()))
                .collect(Collectors.toList());
        validMovesList.forEach(field -> field.setValidMove(false));

        when(fieldLogic.isActivePlayerField(game, from)).thenReturn(true);
        when(pieceLogicFactory.getLogic(PieceType.PAWN)).thenReturn(pawnLogic);
        when(pawnLogic.getValidMoves(game.getGrid(), from)).thenReturn(validMovesList);

        moveLogic.enableValidMoves(game, from);

        assertEquals(NUMBER_OF_SQUARES, game.getGrid().getFields().size());
        assertEquals(2L, game.getGrid().getFields().stream().filter(Field::isValidMove).count());
        assertEquals(validMovesList, game.getGrid().getFields().stream().filter(Field::isValidMove).collect(Collectors.toList()));
    }

    @Test
    void testIsFrom_WhenFieldWithNoPiece_ThenReturnFalse() {
        Game game = new Game().setPlayers(Arrays.asList(new Human().setColor(WHITE), new Human().setColor(BLACK)));
        Field field = new Field();

        assertFalse(moveLogic.isFrom(game, field));
    }

    @Test
    void testIsFrom_WhenFieldWithWhitePawnAndItsBlacksTurn_ThenReturnFalse() {
        Game game = new Game().setPlayers(Arrays.asList(new Human().setColor(WHITE), new Human().setColor(BLACK))).setCurrentPlayerColor(BLACK);
        Field field = new Field().setPiece(new Pawn().setColor(WHITE));

        assertFalse(moveLogic.isFrom(game, field));
    }

    @Test
    void testIsFrom_WhenFieldWithWhitePawnAndItsWhitesTurn_ThenReturnTrue() {
        Game game = new Game().setPlayers(Arrays.asList(new Human().setColor(WHITE), new Human().setColor(BLACK))).setCurrentPlayerColor(WHITE);
        Field field = new Field().setPiece(new Pawn().setColor(WHITE));

        assertTrue(moveLogic.isFrom(game, field));
    }

    @Test
    void testMoveRookWhenCastling_WhenCastling_ThenMoveRook() {
        Board board = new Board(gameService, WHITE);
        Grid grid = new Grid(new ArrayList<>(), gridLogic);
        Game game = new Game().setGrid(grid).setHumanPlayerColor(WHITE);

        Piece rook = new Rook().setColor(WHITE);

        Field kingFrom = new Field().setPiece(new King().setColor(WHITE));
        Field kingTo = new Field().setCoordinates(new Coordinates(3, 1));

        Field rookFrom = new Field().setPiece(rook).setCoordinates(new Coordinates(1, 1));
        rookFrom.setButton(new Button(board, rookFrom));
        rookFrom.getButton().setIcon(new ImageIcon("src/main/resources/images/white_rook.png"));

        Field rookTo = new Field().setCoordinates(new Coordinates(4, 1));
        rookTo.setButton(new Button(board, rookTo));

        when(kingLogic.isValidCastling(game.getGrid(), kingFrom, kingTo, kingTo.getCoordinates().getX(), false, true)).thenReturn(true);
        when(gridLogic.getField(grid, rookFrom.getCoordinates())).thenReturn(rookFrom);
        when(gridLogic.getField(grid, rookTo.getCoordinates())).thenReturn(rookTo);

        moveLogic.moveRookWhenCastling(game, kingFrom, kingTo);

        assertNull(rookFrom.getPiece());
        assertNull(rookFrom.getButton().getIcon());
        assertEquals(rook, rookTo.getPiece());
        assertEquals("src/main/resources/images/white_rook.png", rookTo.getButton().getIcon().toString());
    }

    @Test
    void testResetValidMoves_WhenNotDuringAMove_ThenEnableOrDisableAllMoves() {
        Board board = new Board(gameService, WHITE);

        Game game = new Game().setGrid(new Grid(IntStream.rangeClosed(0, LAST_SQUARE_ON_THE_BOARD_ID).mapToObj(id -> {
            Field field = new Field().setId(id);
            Button button = new Button(board, field);
            button.setEnabled(true);
            return field.setButton(button);
        }).collect(Collectors.toList()), gridLogic));

        moveLogic.resetValidMoves(game, new Move());

        verify(fieldLogic, times(NUMBER_OF_SQUARES)).isActivePlayerField(eq(game), any(Field.class));
    }

    @Test
    void testResetValidMoves_WhenTwoPawnsInvolvedInMovement_Then62MayNotBeCapturedEnPassant() {
        Board board = new Board(gameService, WHITE);

        Game game = new Game().setGrid(new Grid(IntStream.rangeClosed(0, LAST_SQUARE_ON_THE_BOARD_ID).mapToObj(id -> {
            Field field = new Field().setId(id).setPiece(new Pawn().setMayBeCapturedEnPassant(true));
            Button button = new Button(board, field);
            button.setEnabled(true);
            return field.setButton(button);
        }).collect(Collectors.toList()), gridLogic));

        moveLogic.resetValidMoves(game,
                new Move()
                        .setFrom(game
                                .getGrid()
                                .getFields()
                                .stream()
                                .filter(field -> field.getCoordinates().getX() == 5 && field.getCoordinates().getY() == 6)
                                .findFirst()
                                .orElse(null))
                        .setTo(game
                                .getGrid()
                                .getFields()
                                .stream()
                                .filter(field -> field.getCoordinates().getX() == 5 && field.getCoordinates().getY() == 4)
                                .findFirst()
                                .orElse(null)));

        verify(fieldLogic, times(NUMBER_OF_SQUARES)).isActivePlayerField(eq(game), any(Field.class));
        assertEquals(ALL_BUT_TWO_SQUARES,
                game.getGrid().getFields().stream().filter(field -> !((Pawn) field.getPiece()).isMayBeCapturedEnPassant()).count());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenKing_ThenSetHasMovedAtLeastOnce() {
        Grid grid = new Grid(new ArrayList<>(), gridLogic);
        Game game = new Game().setGrid(grid);
        Field from = new Field().setPiece(new King());
        Field to = new Field();

        moveLogic.setChessPieceSpecificFields(game, from, to);

        assertTrue(((King) from.getPiece()).isHasMovedAtLeastOnce());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenPawnIsBeingPromoted_ThenPawnIsPromotedToQueen() {
        Grid grid = new Grid(new ArrayList<>(), gridLogic);
        Game game = new Game().setGrid(grid);
        Field from = new Field().setPiece(new Pawn().setColor(WHITE));
        Field to = new Field();
        Queen queen = new Queen();

        try (MockedStatic<PieceFactory> pieceFactory = Mockito.mockStatic(PieceFactory.class)) {
            pieceFactory.when(() -> PieceFactory.getPiece(PieceType.QUEEN)).thenReturn(queen);
            when(pieceLogicFactory.getLogic(PieceType.PAWN)).thenReturn(pawnLogic);
            when(pawnLogic.isPawnBeingPromoted(from, to)).thenReturn(true);

            moveLogic.setChessPieceSpecificFields(game, from, to);

            verify(pawnLogic).mayBeCapturedEnPassant(game.getGrid(), from, to);
            verify(pawnLogic).isPawnBeingPromoted(from, to);
            verify(fieldLogic).addChessPiece(to, queen.setColor(WHITE));

            assertTrue(from.getPiece().isPawnBeingPromoted());
        }
    }

    @Test
    void testSetChessPieceSpecificFields_WhenPawnMayBeCapturedEnPassant_ThenSetMayBeCapturedEnPassant() {
        Game game = new Game().setGrid(new Grid(new ArrayList<>(), gridLogic));
        Field from = new Field().setPiece(new Pawn());
        Field to = new Field();

        when(pieceLogicFactory.getLogic(PieceType.PAWN)).thenReturn(pawnLogic);
        when(pawnLogic.mayBeCapturedEnPassant(game.getGrid(), from, to)).thenReturn(true);

        moveLogic.setChessPieceSpecificFields(game, from, to);

        verify(pawnLogic).mayBeCapturedEnPassant(game.getGrid(), from, to);
        verify(pawnLogic).isPawnBeingPromoted(from, to);

        assertTrue(((Pawn) from.getPiece()).isMayBeCapturedEnPassant());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenRook_ThenSetHasMovedAtLeastOnce() {
        Game game = new Game().setGrid(new Grid(new ArrayList<>(), gridLogic));
        Field from = new Field().setPiece(new Rook());
        Field to = new Field();

        moveLogic.setChessPieceSpecificFields(game, from, to);

        assertTrue(((Rook) from.getPiece()).isHasMovedAtLeastOnce());
    }
}