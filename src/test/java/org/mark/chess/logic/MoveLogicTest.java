package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.factory.PieceFactory;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
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
    @InjectMocks
    private MoveLogic moveLogic;

    @Mock
    private BackgroundColorFactory backgroundColorFactory;

    @Mock
    private ButtonLogic buttonLogic;

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
        Game game = new Game().setCurrentPlayerId(BLACK.ordinal());
        moveLogic.changeTurn(game);

        assertEquals(WHITE.ordinal(), game.getCurrentPlayerId());
    }

    @Test
    void testChangeTurn_WhenWhite_ThenBlack() {
        Game game = new Game().setCurrentPlayerId(WHITE.ordinal());
        moveLogic.changeTurn(game);

        assertEquals(BLACK.ordinal(), game.getCurrentPlayerId());
    }

    @Test
    void testEnableValidMoves_When64EnabledMovesAnd2ValidMoves_ThenDisable62Moves() {
        Board board = new Board(gameService);

        List<Field> grid = IntStream.rangeClosed(0, 63).mapToObj(id -> {
            Field field = new Field().setId(id).setValidMove(false);
            return field.setButton(new Button(board, field));
        }).collect(Collectors.toList());

        Game game = new Game().setGrid(grid);
        Field from = new Field().setPiece(new Pawn().setColor(WHITE));

        List<Field> validMovesList = grid
                .stream()
                .filter(field -> field.getCoordinates().getX() == 4)
                .filter(field -> Arrays.asList(4, 5).contains(field.getCoordinates().getY()))
                .collect(Collectors.toList());
        validMovesList.forEach(field -> field.setValidMove(false));

        when(fieldLogic.isActivePlayerField(game, from)).thenReturn(true);
        when(pieceLogicFactory.getLogic(PieceType.PAWN)).thenReturn(pawnLogic);
        when(pawnLogic.getValidMoves(grid, from, pieceLogicFactory)).thenReturn(validMovesList);

        moveLogic.enableValidMoves(game, from);

        assertEquals(64, game.getGrid().size());
        assertEquals(2, game.getGrid().stream().filter(Field::isValidMove).count());
        assertEquals(validMovesList, game.getGrid().stream().filter(Field::isValidMove).collect(Collectors.toList()));
    }

    @Test
    void testIsFrom_WhenFieldWithNoPiece_ThenReturnFalse() {
        Game game = new Game().setPlayers(Arrays.asList(new Human().setColor(WHITE), new Human().setColor(Color.BLACK)));
        Field field = new Field();

        assertFalse(moveLogic.isFrom(game, field));
    }

    @Test
    void testIsFrom_WhenFieldWithWhitePawnAndItsBlacksTurn_ThenReturnFalse() {
        Game game = new Game()
                .setPlayers(Arrays.asList(new Human().setColor(WHITE), new Human().setColor(Color.BLACK)))
                .setCurrentPlayerId(Color.BLACK.ordinal());
        Field field = new Field().setPiece(new Pawn().setColor(WHITE));

        assertFalse(moveLogic.isFrom(game, field));
    }

    @Test
    void testIsFrom_WhenFieldWithWhitePawnAndItsWhitesTurn_ThenReturnTrue() {
        Game game = new Game()
                .setPlayers(Arrays.asList(new Human().setColor(WHITE), new Human().setColor(Color.BLACK)))
                .setCurrentPlayerId(WHITE.ordinal());
        Field field = new Field().setPiece(new Pawn().setColor(WHITE));

        assertTrue(moveLogic.isFrom(game, field));
    }

    @Test
    void testMoveRookWhenCastling_WhenCastling_ThenMoveRook() {
        Board board = new Board(gameService);
        List<Field> grid = new ArrayList<>();

        Piece rook = new Rook().setColor(WHITE);

        Field kingFrom = new Field().setPiece(new King().setColor(WHITE));
        Field kingTo = new Field().setCoordinates(new Coordinates(3, 1));

        Field rookFrom = new Field().setPiece(rook).setCoordinates(new Coordinates(1, 1));
        rookFrom.setButton(new Button(board, rookFrom));
        rookFrom.getButton().setIcon(new ImageIcon("src/main/resources/images/white_rook.png"));

        Field rookTo = new Field().setCoordinates(new Coordinates(4, 1));
        rookTo.setButton(new Button(board, rookTo));

        when(kingLogic.isValidCastling(grid, kingFrom, kingTo, kingTo.getCoordinates().getX(), pieceLogicFactory, false, true)).thenReturn(true);
        when(gridLogic.getField(grid, rookFrom.getCoordinates())).thenReturn(rookFrom);
        when(gridLogic.getField(grid, rookTo.getCoordinates())).thenReturn(rookTo);

        moveLogic.moveRookWhenCastling(grid, kingFrom, kingTo);

        assertNull(rookFrom.getPiece());
        assertNull(rookFrom.getButton().getIcon());
        assertEquals(rook, rookTo.getPiece());
        assertEquals("src/main/resources/images/white_rook.png", rookTo.getButton().getIcon().toString());
    }

    @Test
    void testResetValidMoves_WhenNotDuringAMove_ThenEnableOrDisableAllMoves() {
        Board board = new Board(gameService);

        Game game = new Game().setGrid(IntStream.rangeClosed(0, 63).mapToObj(id -> {
            Field field = new Field().setId(id);
            Button button = new Button(board, field);
            button.setEnabled(true);
            return field.setButton(button);
        }).collect(Collectors.toList()));

        moveLogic.resetValidMoves(game, new Move());

        verify(fieldLogic, times(64)).isActivePlayerField(eq(game), any(Field.class));
    }

    @Test
    void testResetValidMoves_WhenTwoPawnsInvolvedInMovement_Then62MayNotBeCapturedEnPassant() {
        Board board = new Board(gameService);

        Game game = new Game().setGrid(IntStream.rangeClosed(0, 63).mapToObj(id -> {
            Field field = new Field().setId(id).setPiece(new Pawn().setMayBeCapturedEnPassant(true));
            Button button = new Button(board, field);
            button.setEnabled(true);
            return field.setButton(button);
        }).collect(Collectors.toList()));

        moveLogic.resetValidMoves(game,
                new Move()
                        .setFrom(game
                                .getGrid()
                                .stream()
                                .filter(field -> field.getCoordinates().getX() == 5 && field.getCoordinates().getY() == 6)
                                .findFirst()
                                .get())
                        .setTo(game
                                .getGrid()
                                .stream()
                                .filter(field -> field.getCoordinates().getX() == 5 && field.getCoordinates().getY() == 4)
                                .findFirst()
                                .get()));

        verify(fieldLogic, times(64)).isActivePlayerField(eq(game), any(Field.class));
        assertEquals(62, game.getGrid().stream().filter(field -> !((Pawn) field.getPiece()).isMayBeCapturedEnPassant()).count());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenKing_ThenSetHasMovedAtLeastOnce() {
        List<Field> grid = new ArrayList<>();
        Game game = new Game().setGrid(grid);
        Field from = new Field().setPiece(new King());
        Field to = new Field();

        moveLogic.setChessPieceSpecificFields(game, from, to);

        assertTrue(((King) from.getPiece()).isHasMovedAtLeastOnce());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenPawnIsBeingPromoted_ThenPawnIsPromotedToQueen() {
        List<Field> grid = new ArrayList<>();
        Game game = new Game().setGrid(grid);
        Field from = new Field().setPiece(new Pawn().setColor(WHITE));
        Field to = new Field();
        Queen queen = new Queen();

        when(pieceLogicFactory.getLogic(PieceType.PAWN)).thenReturn(pawnLogic);
        when(pawnLogic.isPawnBeingPromoted(from, to)).thenReturn(true);
        when(pieceFactory.getPiece(PieceType.QUEEN)).thenReturn(queen);

        moveLogic.setChessPieceSpecificFields(game, from, to);

        verify(pawnLogic).mayBeCapturedEnPassant(game.getGrid(), from, to);
        verify(pawnLogic).isPawnBeingPromoted(from, to);
        verify(fieldLogic).addChessPiece(game, to, queen.setColor(WHITE));

        assertTrue(from.getPiece().isPawnBeingPromoted());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenPawnMayBeCapturedEnPassant_ThenSetMayBeCapturedEnPassant() {
        List<Field> grid = new ArrayList<>();
        Game game = new Game().setGrid(grid);
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
        List<Field> grid = new ArrayList<>();
        Game game = new Game().setGrid(grid);
        Field from = new Field().setPiece(new Rook());
        Field to = new Field();

        moveLogic.setChessPieceSpecificFields(game, from, to);

        assertTrue(((Rook) from.getPiece()).isHasMovedAtLeastOnce());
    }
}