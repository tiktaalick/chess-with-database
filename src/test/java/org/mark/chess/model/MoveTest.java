package org.mark.chess.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.rulesengine.PawnMayBeCapturedEnPassantRulesEngine;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.rulesengine.rule.isvalidmove.KingIsValidCastlingRule;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.QUEEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoveTest {
    private static final int NUMBER_OF_SQUARES = 64;

    @InjectMocks
    private Move move = new Move(new Field(null));

    @Mock
    private Board board;

    @Mock
    private Game game;

    @Mock
    private Button button;

    @Mock
    private PawnMayBeCapturedEnPassantRulesEngine pawnMayBeCapturedEnPassantRulesEngine;

    @Test
    void testChangeTurn_WhenBlack_ThenWhite() {
        Game game = new Game(true, WHITE, Grid.create(board, WHITE)).setCurrentPlayerColor(BLACK);

        move.changeTurn(game);

        assertEquals(WHITE, game.getCurrentPlayerColor());
    }

    @Test
    void testChangeTurn_WhenWhite_ThenBlack() {
        Game game = new Game(true, WHITE, Grid.create(board, WHITE)).setCurrentPlayerColor(WHITE);

        move.changeTurn(game);

        assertEquals(BLACK, game.getCurrentPlayerColor());
    }

    @Test
    void testDuringAMove() {
        Field field = new Field(null).setCode("d4");
        move.setFrom(field);
        move.setTo(new Field(null).setCode("d5"));

        assertTrue(move.duringAMove(field));
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
        when(game.getValidMoves(from)).thenReturn(validMovesList);

        move.enableValidMoves(game, from);

        assertEquals(NUMBER_OF_SQUARES, game.getGrid().getFields().size());
        assertEquals(2L, game.getGrid().getFields().stream().filter(Field::isValidMove).count());
        assertEquals(validMovesList, game.getGrid().getFields().stream().filter(Field::isValidMove).collect(Collectors.toList()));
    }

    @Test
    void testIsFrom_WhenFieldWithNoPiece_ThenReturnFalse() {
        Game game = Game.create(board, WHITE);
        Field field = new Field(null);

        assertFalse(move.isFrom(game, field));
    }

    @Test
    void testIsFrom_WhenFieldWithWhitePawnAndItsBlacksTurn_ThenReturnFalse() {
        Game game = Game.create(board, WHITE).setCurrentPlayerColor(BLACK);
        Field field = new Field(new Pawn(WHITE));

        assertFalse(move.isFrom(game, field));
    }

    @Test
    void testIsFrom_WhenFieldWithWhitePawnAndItsWhitesTurn_ThenReturnTrue() {
        Game game = Game.create(board, WHITE).setCurrentPlayerColor(WHITE);
        Field field = new Field(new Pawn(WHITE));

        assertTrue(move.isFrom(game, field));
    }

    @Test
    void testMoveRookWhenCastling_WhenCastling_ThenMoveRook() {
        Game game = Game.create(board, WHITE);

        Piece rook = new Rook(WHITE);

        Field kingFrom = new Field(new King(WHITE));
        Field kingTo = new Field(null).setCoordinates(new Coordinates(3, 1));

        try (MockedStatic<KingIsValidCastlingRule> kingIsValidCastlingRuleMockedStatic = Mockito.mockStatic(KingIsValidCastlingRule.class)) {
            kingIsValidCastlingRuleMockedStatic
                    .when(() -> KingIsValidCastlingRule.isValidCastling(game.getGrid(),
                            kingFrom,
                            kingTo,
                            kingTo.getCoordinates().getX(),
                            false,
                            true))
                    .thenReturn(true);

            move.moveRookWhenCastling(game, kingFrom, kingTo);

            assertNull(move.getRookMove().getFrom().getPiece());
            assertNull(move.getRookMove().getFrom().getButton().getIcon());
            assertEquals(rook, move.getRookMove().getTo().getPiece());
        }
    }

    @Test
    void testSetChessPieceSpecificFields_WhenKing_ThenSetHasMovedAtLeastOnce() {
        Grid grid = Grid.create(board, WHITE);
        Game game = Game.create(board, WHITE).setGrid(grid);
        Field from = new Field(new King(WHITE));
        Field to = new Field(null);

        move.setPieceTypeSpecificFields(game, from, to);

        assertTrue(((King) from.getPiece()).isHasMovedAtLeastOnce());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenPawnIsBeingPromoted_ThenPawnIsPromotedToQueen() {
        Grid grid = Grid.create(board, WHITE);
        Game game = Game.create(board, WHITE).setGrid(grid);
        Field from = new Field(new Pawn(WHITE)).setButton(button);
        Field to = new Field(null).setButton(button).setCode("e8");

        move.setPieceTypeSpecificFields(game, from, to);

        assertTrue(from.getPiece().isPawnBeingPromoted());
        assertEquals(QUEEN, to.getPiece().getPieceType());
        assertEquals(WHITE, to.getPiece().getColor());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenPawnMayBeCapturedEnPassant_ThenSetMayBeCapturedEnPassant() {
        Grid grid = Grid.create(board, WHITE);
        Game game = Game.create(board, WHITE).setGrid(grid);
        Field from = new Field(new Pawn(WHITE).setPawnMayBeCapturedEnPassantRulesEngine(pawnMayBeCapturedEnPassantRulesEngine));
        Field to = new Field(null);

        when(pawnMayBeCapturedEnPassantRulesEngine.process(any(IsValidMoveParameter.class))).thenReturn(true);

        move.setPieceTypeSpecificFields(game, from, to);

        assertTrue(((Pawn) from.getPiece()).isMayBeCapturedEnPassant());
    }

    @Test
    void testSetChessPieceSpecificFields_WhenRook_ThenSetHasMovedAtLeastOnce() {
        Grid grid = Grid.create(board, WHITE);
        Game game = Game.create(board, WHITE).setGrid(grid);
        Field from = new Field(new Rook(WHITE));
        Field to = new Field(null);

        move.setPieceTypeSpecificFields(game, from, to);

        assertTrue(((Rook) from.getPiece()).isHasMovedAtLeastOnce());
    }
}