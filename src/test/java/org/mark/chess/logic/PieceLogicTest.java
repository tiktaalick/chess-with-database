package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.King;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.Queen;
import org.mark.chess.service.GameService;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.KING;
import static org.mark.chess.enums.PieceType.QUEEN;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PieceLogicTest {
    @Spy
    private PieceLogic pieceLogic;

    @Mock
    private PieceLogicFactory opponentFactory;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private GameService gameService;

    @Test
    void testGetValidMoves_WhenNoValidMoves_ThenReturnOnlyFromMove() {
        Field from = new Field().setCoordinates(new Coordinates(0, 0));
        Field invalidTo = new Field().setCoordinates(new Coordinates(2, 2));

        List<Field> grid = new ArrayList<>(Arrays.asList(from, invalidTo));

        when(pieceLogic.isValidMove(grid, from, from, opponentFactory, false)).thenReturn(false);
        when(pieceLogic.isValidMove(grid, from, invalidTo, opponentFactory, false)).thenReturn(false);

        List<Field> validMoves = pieceLogic.getValidMoves(grid, from, opponentFactory);

        assertEquals(0, validMoves.size());
    }

    @Test
    void testGetValidMoves_WhenOneValidMove_ThenReturnValidMoveAndFromMove() {
        Field from = new Field().setCoordinates(new Coordinates(0, 0));
        Field validTo = new Field().setCoordinates(new Coordinates(1, 1));
        Field invalidTo = new Field().setCoordinates(new Coordinates(2, 2));

        List<Field> grid = new ArrayList<>(Arrays.asList(from, validTo, invalidTo));

        when(pieceLogic.isValidMove(grid, from, from, opponentFactory, false)).thenReturn(false);
        when(pieceLogic.isValidMove(grid, from, validTo, opponentFactory, false)).thenReturn(true);
        when(pieceLogic.isValidMove(grid, from, invalidTo, opponentFactory, false)).thenReturn(false);

        List<Field> validMoves = pieceLogic.getValidMoves(grid, from, opponentFactory);

        assertEquals(1, validMoves.size());
    }

    @Test
    void testIsFriendlyFire_WhenPawnMovesToEmptyField_ThenReturnFalse() {
        assertFalse(pieceLogic.isFriendlyFire(new Pawn().setColor(WHITE), new Field()));
    }

    @Test
    void testIsFriendlyFire_WhenPawnMovesToFieldWithBlackQueen_ThenReturnFalse() {
        assertFalse(pieceLogic.isFriendlyFire(new Pawn().setColor(WHITE), new Field().setPiece(new Queen().setColor(BLACK))));
    }

    @Test
    void testIsFriendlyFire_WhenPawnMovesToFieldWithWhiteQueen_ThenReturnTrue() {
        assertTrue(pieceLogic.isFriendlyFire(new Pawn().setColor(WHITE), new Field().setPiece(new Queen().setColor(WHITE))));
    }

    @Test
    void testIsJumping_WhenWhitePawnMovesTwoStepsAndDoesNotJumpOverAPiece_ThenReturnFalse() {
        List<Field> grid = IntStream.rangeClosed(0, 63).mapToObj(id -> new Field().setId(id)).collect(Collectors.toList());

        Field from = grid.stream().filter(field -> field.getCode().equals("e2")).findFirst().get().setPiece(new Pawn().setColor(WHITE));
        Field to = grid.stream().filter(field -> field.getCode().equals("e4")).findFirst().get();

        assertFalse(pieceLogic.isJumping(grid, from, to));
    }

    @Test
    void testIsJumping_WhenWhitePawnMovesTwoStepsAndJumpsOverAPiece_ThenReturnTrue() {
        List<Field> grid = IntStream.rangeClosed(0, 63).mapToObj(id -> new Field().setId(id)).collect(Collectors.toList());

        Field from = grid.stream().filter(field -> field.getCode().equals("e2")).findFirst().get().setPiece(new Pawn().setColor(WHITE));
        Field to = grid.stream().filter(field -> field.getCode().equals("e4")).findFirst().get();

        grid.stream().filter(field -> field.getCode().equals("e3")).findFirst().get().setPiece(new Pawn().setColor(WHITE));

        assertTrue(pieceLogic.isJumping(grid, from, to));
    }

    @Test
    void testIsJumping_WhenWhitePawnMovesTwoStepsAndEndsOnAPiece_ThenReturnFalse() {
        List<Field> grid = IntStream.rangeClosed(0, 63).mapToObj(id -> new Field().setId(id)).collect(Collectors.toList());

        Field from = grid.stream().filter(field -> field.getCode().equals("e2")).findFirst().get().setPiece(new Pawn().setColor(WHITE));
        Field to = grid.stream().filter(field -> field.getCode().equals("e4")).findFirst().get();

        grid.stream().filter(field -> field.getCode().equals("e4")).findFirst().get().setPiece(new Pawn().setColor(WHITE));

        assertFalse(pieceLogic.isJumping(grid, from, to));
    }

    @Test
    void testGetAbsoluteHorizontalMove() {
        assertEquals(3,
                pieceLogic.getAbsoluteHorizontalMove(new Field().setCoordinates(new Coordinates(2, 3)),
                        new Field().setCoordinates(new Coordinates(5, 8))));
    }

    @Test
    void testGetAbsoluteVerticalMove() {
        assertEquals(5,
                pieceLogic.getAbsoluteVerticalMove(new Field().setCoordinates(new Coordinates(2, 3)),
                        new Field().setCoordinates(new Coordinates(5, 8))));
    }

    @Test
    void testHasEmptyParameters_WhenEmptyGrid_ThenTrue() {
        assertTrue(pieceLogic.hasEmptyParameters(null, new Field(), new Field(), new PieceLogicFactory()));
    }

    @Test
    void testHasEmptyParameters_WhenEmptyFrom_ThenTrue() {
        assertTrue(pieceLogic.hasEmptyParameters(new ArrayList<>(), null, new Field(), new PieceLogicFactory()));
    }

    @Test
    void testHasEmptyParameters_WhenEmptyTo_ThenTrue() {
        assertTrue(pieceLogic.hasEmptyParameters(new ArrayList<>(), new Field(), null, new PieceLogicFactory()));
    }

    @Test
    void testHasEmptyParameters_WhenEmptyPieceLogicFactory_ThenTrue() {
        assertTrue(pieceLogic.hasEmptyParameters(new ArrayList<>(), new Field(), new Field(), null));
    }

    @Test
    void testHasEmptyParameters_WhenNoEmptyParameters_ThenFalse() {
        assertFalse(pieceLogic.hasEmptyParameters(new ArrayList<>(), new Field(), new Field(), new PieceLogicFactory()));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "true;d3;e2;h2;king;e2;false",
            "false;d3;e2;h2;king;e2;true",
            "false;d3;e2;h2;king;e1;false",
            "false;d3;e2;h2;king;f1;true",
            "false;d3;e2;h2;king;d3;false",
            "false;d3;f3;e3;pawn;e4;true",
            "false;d3;f3;e2;pawn;e3;false",
            "false;d3;f3;e2;pawn;d3;false",
            "false;c5;c4;h2;king;b5;true",
            "false;c5;c4;h2;king;c5;false",
            "false;c5;c4;h2;king;d5;true",
            "false;c5;c4;h2;king;b4;true",
            "false;c5;c4;h2;king;c4;true",
            "false;c5;c4;h2;king;d4;true",
            "false;c5;c4;h2;king;b3;false",
            "false;c5;c4;h2;king;c3;true",
            "false;c5;c4;h2;king;d3;false",}, delimiter = ';')
    @MockitoSettings(strictness = Strictness.LENIENT)
    void testIsMovingIntoCheck_WhenMovingIntoCheck_ThenReturnTrue(boolean isOpponent,
            String codeOpponentQueen,
            String codeKing,
            String codePawn,
            String nameOfMovingPiece,
            String toCode,
            boolean expectedOutcome) {
        List<Field> grid = IntStream.rangeClosed(0, 63).mapToObj(id -> new Field().setId(id)).collect(Collectors.toList());

        Field queenField = grid
                .stream()
                .filter(field -> field.getCode().equals(codeOpponentQueen))
                .findFirst()
                .orElse(new Field())
                .setPiece(new Queen().setColor(BLACK));
        Field kingFieldBeforeMove = grid
                .stream()
                .filter(field -> field.getCode().equals(codeKing))
                .findFirst()
                .orElse(new Field())
                .setPiece(new King().setColor(WHITE));
        Field pawnField = grid
                .stream()
                .filter(field -> field.getCode().equals(codePawn))
                .findFirst()
                .orElse(new Field())
                .setPiece(new Pawn().setColor(WHITE));

        grid.set(queenField.getId(), queenField);
        grid.set(kingFieldBeforeMove.getId(), kingFieldBeforeMove);
        grid.set(pawnField.getId(), pawnField);

        Field fromField = grid
                .stream()
                .filter(field -> field.getPiece() != null && field.getPiece().getPieceType().getName().equals(nameOfMovingPiece))
                .findFirst()
                .orElse(null);
        Field toField = grid.stream().filter(field -> field.getCode().equals(toCode)).findFirst().orElse(fromField);

        Field kingFieldAfterMove = new Field().setId(nameOfMovingPiece.equals(KING.getName())
                ? toField.getId()
                : kingFieldBeforeMove.getId());

        when(gridLogic.getKingField(anyList(), eq(WHITE))).thenReturn(kingFieldAfterMove);
        when(opponentFactory.getLogic(QUEEN)).thenReturn(new QueenLogic());

        assertEquals(expectedOutcome, pieceLogic.isMovingIntoCheck(grid, fromField, toField, isOpponent, opponentFactory, gridLogic));
    }
}