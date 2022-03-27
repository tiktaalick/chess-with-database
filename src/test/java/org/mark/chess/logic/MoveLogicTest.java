package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.logic.GridLogic.NUMBER_OF_COLUMNS_AND_ROWS;
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
    private GameService gameService;

    @Mock
    private PieceLogicFactory pieceLogicFactory;

    @Mock
    private PieceFactory pieceFactory;

    @Mock
    private PawnLogic pawnLogic;

    @Mock
    private KingLogic kingLogic;

    @Mock
    private FieldLogic fieldLogic;

    @Mock
    private GridLogic gridLogic;

    @Test
    void testIsFrom_WhenFieldWithWhitePawnAndItsWhitesTurn_ThenReturnTrue() {
        Game game = new Game().players(Arrays.asList(new Human().color(WHITE), new Human().color(Color.BLACK))).currentPlayerIndex(WHITE.ordinal());
        Field field = new Field().piece(new Pawn().color(WHITE));

        assertTrue(moveLogic.isFrom(game, field));
    }

    @Test
    void testIsFrom_WhenFieldWithWhitePawnAndItsBlacksTurn_ThenReturnFalse() {
        Game game = new Game()
                .players(Arrays.asList(new Human().color(WHITE), new Human().color(Color.BLACK)))
                .currentPlayerIndex(Color.BLACK.ordinal());
        Field field = new Field().piece(new Pawn().color(WHITE));

        assertFalse(moveLogic.isFrom(game, field));
    }

    @Test
    void testIsFrom_WhenFieldWithNoPiece_ThenReturnFalse() {
        Game game = new Game().players(Arrays.asList(new Human().color(WHITE), new Human().color(Color.BLACK)));
        Field field = new Field();

        assertFalse(moveLogic.isFrom(game, field));
    }

    @Test
    void testEnableValidMoves_When64EnabledMovesAnd2ValidMoves_ThenDisable62Moves() {
        Board board = new Board(gameService);
        List<Field> grid = new ArrayList<>();
        Game game = new Game().grid(grid);
        Field from = new Field().piece(new Pawn().color(WHITE));

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Field field = new Field().coordinates(new Coordinates(x, y)).id(y * NUMBER_OF_COLUMNS_AND_ROWS + x);
                Button button = new Button(board, field);
                button.setEnabled(true);
                grid.add(field.button(button));
            }
        }

        List<Field> validMovesList = grid
                .stream()
                .filter(field -> field.coordinates().x() == 4)
                .filter(field -> Arrays.asList(4, 5).contains(field.coordinates().y()))
                .collect(Collectors.toList());
        validMovesList.forEach(field -> field.button().setEnabled(false));

        when(pieceLogicFactory.getLogic(PieceType.PAWN)).thenReturn(pawnLogic);
        when(pawnLogic.getValidMoves(grid, from, pieceLogicFactory)).thenReturn(validMovesList);

        moveLogic.enableValidMoves(game, from);

        assertEquals(64, game.grid().size());
        assertEquals(2, game.grid().stream().filter(field -> field.button().isEnabled()).count());
        assertEquals(validMovesList, game.grid().stream().filter(field -> field.button().isEnabled()).collect(Collectors.toList()));
    }

    @Test
    void testResetValidMoves_WhenNotDuringAMove_ThenEnableOrDisableAllMoves() {
        Board board = new Board(gameService);
        List<Field> grid = new ArrayList<>();
        Game game = new Game().grid(grid);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Field field = new Field().coordinates(new Coordinates(x, y)).id(y * NUMBER_OF_COLUMNS_AND_ROWS + x);
                Button button = new Button(board, field);
                button.setEnabled(true);
                grid.add(field.button(button));
            }
        }

        moveLogic.resetValidMoves(game, new Move());

        verify(fieldLogic, times(64)).setEnabledButton(eq(game), any(Field.class));
    }

    @Test
    void testResetValidMoves_WhenTwoPawnsInvolvedInMovement_Then62MayNotBeCapturedEnPassant() {
        Board board = new Board(gameService);
        List<Field> grid = new ArrayList<>();
        Game game = new Game().grid(grid);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Field field = new Field()
                        .coordinates(new Coordinates(x, y))
                        .id(y * NUMBER_OF_COLUMNS_AND_ROWS + x)
                        .piece(new Pawn().mayBeCapturedEnPassant(true));
                Button button = new Button(board, field);
                button.setEnabled(true);

                grid.add(field.button(button));
            }
        }

        moveLogic.resetValidMoves(game, new Move()
                .from(game.grid().stream().filter(field -> field.coordinates().x() == 5 && field.coordinates().y() == 6).findFirst().get())
                .to(game.grid().stream().filter(field -> field.coordinates().x() == 5 && field.coordinates().y() == 4).findFirst().get()));

        verify(fieldLogic, times(64)).setEnabledButton(eq(game), any(Field.class));
        assertEquals(62, game.grid().stream().filter(field -> !((Pawn) field.piece()).mayBeCapturedEnPassant()).count());
    }

    @Test
    void testResetValidMoves_WhenTwoRooksInvolvedInMovement_Then62HaveNotMovedAtLeastOnce() {
        Board board = new Board(gameService);
        List<Field> grid = new ArrayList<>();
        Game game = new Game().grid(grid);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Field field = new Field()
                        .coordinates(new Coordinates(x, y))
                        .id(y * NUMBER_OF_COLUMNS_AND_ROWS + x)
                        .piece(new Rook().hasMovedAtLeastOnce(true));
                Button button = new Button(board, field);
                button.setEnabled(true);

                grid.add(field.button(button));
            }
        }

        moveLogic.resetValidMoves(game, new Move()
                .from(game.grid().stream().filter(field -> field.coordinates().x() == 5 && field.coordinates().y() == 6).findFirst().get())
                .to(game.grid().stream().filter(field -> field.coordinates().x() == 5 && field.coordinates().y() == 4).findFirst().get()));

        verify(fieldLogic, times(64)).setEnabledButton(eq(game), any(Field.class));
        assertEquals(62, game.grid().stream().filter(field -> !((Rook) field.piece()).hasMovedAtLeastOnce()).count());
    }

    @Test
    void testResetValidMoves_WhenTwoKingsInvolvedInMovement_Then62HaveNotMovedAtLeastOnce() {
        Board board = new Board(gameService);
        List<Field> grid = new ArrayList<>();
        Game game = new Game().grid(grid);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Field field = new Field()
                        .coordinates(new Coordinates(x, y))
                        .id(y * NUMBER_OF_COLUMNS_AND_ROWS + x)
                        .piece(new King().hasMovedAtLeastOnce(true));
                Button button = new Button(board, field);
                button.setEnabled(true);

                grid.add(field.button(button));
            }
        }

        moveLogic.resetValidMoves(game, new Move()
                .from(game.grid().stream().filter(field -> field.coordinates().x() == 5 && field.coordinates().y() == 6).findFirst().get())
                .to(game.grid().stream().filter(field -> field.coordinates().x() == 5 && field.coordinates().y() == 4).findFirst().get()));

        verify(fieldLogic, times(64)).setEnabledButton(eq(game), any(Field.class));
        assertEquals(62, game.grid().stream().filter(field -> !((King) field.piece()).hasMovedAtLeastOnce()).count());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenRook_ThenSetHasMovedAtLeastOnce() {
        List<Field> grid = new ArrayList<>();
        Game game = new Game().grid(grid);
        Field from = new Field().piece(new Rook());
        Field to = new Field();

        moveLogic.setChessPieceSpecificFields(game, from, to);

        assertTrue(((Rook) from.piece()).hasMovedAtLeastOnce());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenKing_ThenSetHasMovedAtLeastOnce() {
        List<Field> grid = new ArrayList<>();
        Game game = new Game().grid(grid);
        Field from = new Field().piece(new King());
        Field to = new Field();

        moveLogic.setChessPieceSpecificFields(game, from, to);

        assertTrue(((King) from.piece()).hasMovedAtLeastOnce());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenPawnMayBeCapturedEnPassant_ThenSetMayBeCapturedEnPassant() {
        List<Field> grid = new ArrayList<>();
        Game game = new Game().grid(grid);
        Field from = new Field().piece(new Pawn());
        Field to = new Field();

        when(pieceLogicFactory.getLogic(PieceType.PAWN)).thenReturn(pawnLogic);
        when(pawnLogic.mayBeCapturedEnPassant(game.grid(), from, to)).thenReturn(true);

        moveLogic.setChessPieceSpecificFields(game, from, to);

        verify(pawnLogic).mayBeCapturedEnPassant(game.grid(), from, to);
        verify(pawnLogic).isPawnBeingPromoted(from, to);

        assertTrue(((Pawn) from.piece()).mayBeCapturedEnPassant());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenPawnIsBeingPromoted_ThenPawnIsPromotedToQueen() {
        List<Field> grid = new ArrayList<>();
        Game game = new Game().grid(grid);
        Field from = new Field().piece(new Pawn().color(WHITE));
        Field to = new Field();
        Queen queen = new Queen();

        when(pieceLogicFactory.getLogic(PieceType.PAWN)).thenReturn(pawnLogic);
        when(pawnLogic.isPawnBeingPromoted(from, to)).thenReturn(true);
        when(pieceFactory.getPiece(PieceType.QUEEN)).thenReturn(queen);

        moveLogic.setChessPieceSpecificFields(game, from, to);

        verify(pawnLogic).mayBeCapturedEnPassant(game.grid(), from, to);
        verify(pawnLogic).isPawnBeingPromoted(from, to);
        verify(gridLogic).addChessPiece(game, to.id(), queen, WHITE);

        assertTrue(from.piece().isPawnBeingPromoted());
    }

    @Test
    void testMoveRookWhenCastling_WhenCastling_ThenMoveRook() {
        Board board = new Board(gameService);
        List<Field> grid = new ArrayList<>();

        Piece rook = new Rook().color(WHITE);

        Field kingFrom = new Field().piece(new King().color(WHITE));
        Field kingTo = new Field().coordinates(new Coordinates(2, 7));

        Field rookFrom = new Field().piece(rook).coordinates(new Coordinates(0, 7));
        rookFrom.button(new Button(board, rookFrom));
        rookFrom.button().setIcon(new ImageIcon("src/main/resources/images/white_rook.png"));

        Field rookTo = new Field().coordinates(new Coordinates(3, 7));
        rookTo.button(new Button(board, rookTo));

        when(kingLogic.isValidCastling(grid, kingFrom, kingTo, kingTo.coordinates().x(), pieceLogicFactory, false, true)).thenReturn(true);
        when(gridLogic.getField(grid, rookFrom.coordinates())).thenReturn(rookFrom);
        when(gridLogic.getField(grid, rookTo.coordinates())).thenReturn(rookTo);

        moveLogic.moveRookWhenCastling(grid, kingFrom, kingTo);

        assertNull(rookFrom.piece());
        assertNull(rookFrom.button().getIcon());
        assertEquals(rook, rookTo.piece());
        assertEquals("src/main/resources/images/white_rook.png", rookTo.button().getIcon().toString());
    }

    @Test
    void testChangeTurn_WhenWhite_ThenBlack() {
        Game game = new Game().currentPlayerIndex(WHITE.ordinal());
        moveLogic.changeTurn(game);

        assertEquals(BLACK.ordinal(), game.currentPlayerIndex());
    }

    @Test
    void testChangeTurn_WhenBlack_ThenWhite() {
        Game game = new Game().currentPlayerIndex(BLACK.ordinal());
        moveLogic.changeTurn(game);

        assertEquals(WHITE.ordinal(), game.currentPlayerIndex());
    }
}