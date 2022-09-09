package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Human;
import org.mark.chess.model.King;
import org.mark.chess.model.Move;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Piece;
import org.mark.chess.model.PieceTypeLogic;
import org.mark.chess.model.Queen;
import org.mark.chess.model.Rook;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import static org.mark.chess.enums.PieceType.PAWN;
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
    private GridLogic gridLogic;

    @Mock
    private KingLogic kingLogic;

    @Mock
    private PawnLogic pawnLogic;

    @Mock
    private PieceTypeLogic pieceTypeLogic;

    @Mock
    private Button button;

    @Mock
    private ColorLogic colorLogic;

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
        Grid grid = Grid.createGrid(IntStream.rangeClosed(0, LAST_SQUARE_ON_THE_BOARD_ID).mapToObj(id -> {
            Field field = new Field(null).setId(id).setValidMove(false);
            return field.setButton(button);
        }).collect(Collectors.toList()), gridLogic, fieldLogic);

        Game game = new Game().setGrid(grid);
        Field from = new Field(new Pawn(WHITE));

        List<Field> validMovesList = grid
                .getFields()
                .stream()
                .filter(field -> field.getCoordinates().getX() == 4)
                .filter(field -> Arrays.asList(4, 5).contains(field.getCoordinates().getY()))
                .collect(Collectors.toList());
        validMovesList.forEach(field -> field.setValidMove(false));

        when(fieldLogic.isActivePlayerField(game, from)).thenReturn(true);
        when(PAWN.getLogic(pieceTypeLogic)).thenReturn(pawnLogic);
        when(pawnLogic.getValidMoves(game.getGrid(), from)).thenReturn(validMovesList);

        moveLogic.enableValidMoves(game, from);

        assertEquals(NUMBER_OF_SQUARES, game.getGrid().getFields().size());
        assertEquals(2L, game.getGrid().getFields().stream().filter(Field::isValidMove).count());
        assertEquals(validMovesList, game.getGrid().getFields().stream().filter(Field::isValidMove).collect(Collectors.toList()));
    }

    @Test
    void testIsFrom_WhenFieldWithNoPiece_ThenReturnFalse() {
        Game game = new Game().setPlayers(Arrays.asList(new Human().setColor(WHITE), new Human().setColor(BLACK)));
        Field field = new Field(null);

        assertFalse(moveLogic.isFrom(game, field));
    }

    @Test
    void testIsFrom_WhenFieldWithWhitePawnAndItsBlacksTurn_ThenReturnFalse() {
        Game game = new Game().setPlayers(Arrays.asList(new Human().setColor(WHITE), new Human().setColor(BLACK))).setCurrentPlayerColor(BLACK);
        Field field = new Field(new Pawn(WHITE));

        assertFalse(moveLogic.isFrom(game, field));
    }

    @Test
    void testIsFrom_WhenFieldWithWhitePawnAndItsWhitesTurn_ThenReturnTrue() {
        Game game = new Game().setPlayers(Arrays.asList(new Human().setColor(WHITE), new Human().setColor(BLACK))).setCurrentPlayerColor(WHITE);
        Field field = new Field(new Pawn(WHITE));

        assertTrue(moveLogic.isFrom(game, field));
    }

    @Test
    void testMoveRookWhenCastling_WhenCastling_ThenMoveRook() {
        Grid grid = Grid.createGrid(new ArrayList<>(), gridLogic, fieldLogic);
        Game game = new Game().setGrid(grid).setHumanPlayerColor(WHITE);

        Piece rook = new Rook(WHITE);

        Field kingFrom = new Field(new King(WHITE));
        Field kingTo = new Field(null).setCoordinates(new Coordinates(3, 1));

        Field rookFrom = new Field(rook).setCoordinates(new Coordinates(1, 1));
        rookFrom.setButton(button);

        Field rookTo = new Field(null).setCoordinates(new Coordinates(4, 1));
        rookTo.setButton(button);

        when(kingLogic.isValidCastling(game.getGrid(), kingFrom, kingTo, kingTo.getCoordinates().getX(), false, true)).thenReturn(true);
        when(gridLogic.getField(grid, rookFrom.getCoordinates())).thenReturn(rookFrom);
        when(gridLogic.getField(grid, rookTo.getCoordinates())).thenReturn(rookTo);

        moveLogic.moveRookWhenCastling(game, kingFrom, kingTo);

        assertNull(rookFrom.getPiece());
        assertNull(rookFrom.getButton().getIcon());
        assertEquals(rook, rookTo.getPiece());
    }

    @Test
    void testResetValidMoves_WhenNotDuringAMove_ThenEnableOrDisableAllMoves() {
        Game game = new Game().setGrid(Grid.createGrid(IntStream.rangeClosed(0, LAST_SQUARE_ON_THE_BOARD_ID).mapToObj(id -> {
            Field field = new Field(null).setId(id);
            return field.setButton(button);
        }).collect(Collectors.toList()), gridLogic, fieldLogic));

        moveLogic.resetValidMoves(game, new Move());

        verify(fieldLogic, times(NUMBER_OF_SQUARES)).isActivePlayerField(eq(game), any(Field.class));
    }

    @Test
    void testResetValidMoves_WhenTwoPawnsInvolvedInMovement_Then62MayNotBeCapturedEnPassant() {
        Game game = new Game().setGrid(Grid.createGrid(IntStream.rangeClosed(0, LAST_SQUARE_ON_THE_BOARD_ID).mapToObj(id -> {
            Field field = new Field(new Pawn(WHITE).setMayBeCapturedEnPassant(true)).setId(id);
            return field.setButton(button);
        }).collect(Collectors.toList()), gridLogic, fieldLogic));

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
        Grid grid = Grid.createGrid(new ArrayList<>(), gridLogic, fieldLogic);
        Game game = new Game().setGrid(grid);
        Field from = new Field(new King(WHITE));
        Field to = new Field(null);

        moveLogic.setChessPieceSpecificFields(game, from, to);

        assertTrue(((King) from.getPiece()).isHasMovedAtLeastOnce());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenPawnIsBeingPromoted_ThenPawnIsPromotedToQueen() {
        Grid grid = Grid.createGrid(new ArrayList<>(), gridLogic, fieldLogic);
        Game game = new Game().setGrid(grid);
        Field from = new Field(new Pawn(WHITE));
        Field to = new Field(null);
        Queen queen = new Queen(WHITE);

        when(PAWN.getLogic(pieceTypeLogic)).thenReturn(pawnLogic);
        when(pawnLogic.isPawnBeingPromoted(from, to)).thenReturn(true);

        moveLogic.setChessPieceSpecificFields(game, from, to);

        verify(pawnLogic).mayBeCapturedEnPassant(game.getGrid(), from, to);
        verify(pawnLogic).isPawnBeingPromoted(from, to);
        verify(fieldLogic).addChessPiece(to, queen.setColor(WHITE));

        assertTrue(from.getPiece().isPawnBeingPromoted());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenPawnMayBeCapturedEnPassant_ThenSetMayBeCapturedEnPassant() {
        Game game = new Game().setGrid(Grid.createGrid(new ArrayList<>(), gridLogic, fieldLogic));
        Field from = new Field(new Pawn(WHITE));
        Field to = new Field(null);

        when(PAWN.getLogic(pieceTypeLogic)).thenReturn(pawnLogic);
        when(pawnLogic.mayBeCapturedEnPassant(game.getGrid(), from, to)).thenReturn(true);

        moveLogic.setChessPieceSpecificFields(game, from, to);

        verify(pawnLogic).mayBeCapturedEnPassant(game.getGrid(), from, to);
        verify(pawnLogic).isPawnBeingPromoted(from, to);

        assertTrue(((Pawn) from.getPiece()).isMayBeCapturedEnPassant());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenRook_ThenSetHasMovedAtLeastOnce() {
        Game game = new Game().setGrid(Grid.createGrid(new ArrayList<>(), gridLogic, fieldLogic));
        Field from = new Field(new Rook(WHITE));
        Field to = new Field(null);

        moveLogic.setChessPieceSpecificFields(game, from, to);

        assertTrue(((Rook) from.getPiece()).isHasMovedAtLeastOnce());
    }
}