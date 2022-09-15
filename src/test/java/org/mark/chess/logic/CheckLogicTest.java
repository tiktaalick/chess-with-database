package org.mark.chess.logic;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.King;
import org.mark.chess.model.Pawn;
import org.mark.chess.model.PieceTypeLogic;
import org.mark.chess.model.Queen;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.QUEEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckLogicTest {
    private static final char DELIMITER     = ';';
    private static final int  MAX_SQUARE_ID = 63;

    @InjectMocks
    private CheckLogic checkLogic;

    @Mock
    private Button button;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private PieceTypeLogic pieceTypeLogic;

    @Mock
    private QueenLogic queenLogic;

    @Mock
    private ColorLogic colorLogic;

    @ParameterizedTest
    @CsvSource(value = {
            "true;d3;e2;h2;king;e2;false",
            "false;d3;e2;h2;king;e2;false",
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
            "false;c5;c4;h2;king;c4;false",
            "false;c5;c4;h2;king;d4;true",
            "false;c5;c4;h2;king;b3;false",
            "false;c5;c4;h2;king;c3;true",
            "false;c5;c4;h2;king;d3;false",}, delimiter = DELIMITER)
    @MockitoSettings(strictness = Strictness.LENIENT)
    void testIsInCheckNow_WhenIsInCheck_ThenReturnTrue(boolean isOpponent,
            String codeOpponentQueen,
            String codeKing,
            String codePawn,
            String nameOfMovingPiece,
            String toCode,
            boolean expectedOutcome) {

        List<Field> fields = IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> {
            Field field = new Field(null).setId(id);
            return field.setButton(button);
        }).collect(Collectors.toList());

        Field queenField = fields
                .stream()
                .filter(field -> field.getCode().equals(codeOpponentQueen))
                .findFirst()
                .orElse(new Field(null))
                .setPiece(new Queen(BLACK));
        Field kingFieldBeforeMove = fields
                .stream()
                .filter(field -> field.getCode().equals(codeKing))
                .findFirst()
                .orElse(new Field(null))
                .setPiece(new King(WHITE));
        Field pawnField = fields
                .stream()
                .filter(field -> field.getCode().equals(codePawn))
                .findFirst()
                .orElse(new Field(null))
                .setPiece(new Pawn(WHITE));

        fields.set(queenField.getId(), queenField);
        fields.set(kingFieldBeforeMove.getId(), kingFieldBeforeMove);
        fields.set(pawnField.getId(), pawnField);

        Field fromField = fields
                .stream()
                .filter(field -> field.getPiece() != null && field.getPiece().getPieceType().getName().equals(nameOfMovingPiece))
                .findFirst()
                .orElse(null);
        Field toField = fields.stream().filter(field -> field.getCode().equals(toCode)).findFirst().orElse(fromField);

        Grid grid = Grid.createGrid(fields, gridLogic).setKingField(kingFieldBeforeMove);

        try (MockedStatic<Grid> gridMockedStatic = Mockito.mockStatic(Grid.class)) {
            gridMockedStatic.when(() -> Grid.createGrid(grid, fromField, toField, gridLogic)).thenReturn(grid);
            when(QUEEN.getLogic(pieceTypeLogic)).thenReturn(queenLogic);
            when(queenLogic.isValidMove(any(Grid.class), any(Field.class), any(Field.class), eq(true))).thenReturn(expectedOutcome);

            assertEquals(expectedOutcome, checkLogic.isInCheckNow(grid, fromField, toField, isOpponent));
            verify(colorLogic,
                    times(expectedOutcome
                            ? 1
                            : 0)).setAttacking(eq(grid), any(Field.class));
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
            "true;d3;e2;h2;king;e2;false",
            "false;d3;e2;h2;king;e2;false",
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
            "false;c5;c4;h2;king;c4;false",
            "false;c5;c4;h2;king;d4;true",
            "false;c5;c4;h2;king;b3;false",
            "false;c5;c4;h2;king;c3;true",
            "false;c5;c4;h2;king;d3;false",}, delimiter = DELIMITER)
    @MockitoSettings(strictness = Strictness.LENIENT)
    void testIsMovingIntoCheck_WhenMovingIntoCheck_ThenReturnTrue(boolean isOpponent,
            String codeOpponentQueen,
            String codeKing,
            String codePawn,
            String nameOfMovingPiece,
            String toCode,
            boolean expectedOutcome) {

        List<Field> fields = IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> {
            Field field = new Field(null).setId(id);
            return field.setButton(button);
        }).collect(Collectors.toList());

        Field queenField = fields
                .stream()
                .filter(field -> field.getCode().equals(codeOpponentQueen))
                .findFirst()
                .orElse(new Field(null))
                .setPiece(new Queen(BLACK));
        Field kingFieldBeforeMove = fields
                .stream()
                .filter(field -> field.getCode().equals(codeKing))
                .findFirst()
                .orElse(new Field(null))
                .setPiece(new King(WHITE));
        Field pawnField = fields
                .stream()
                .filter(field -> field.getCode().equals(codePawn))
                .findFirst()
                .orElse(new Field(null))
                .setPiece(new Pawn(WHITE));

        fields.set(queenField.getId(), queenField);
        fields.set(kingFieldBeforeMove.getId(), kingFieldBeforeMove);
        fields.set(pawnField.getId(), pawnField);

        Field fromField = fields
                .stream()
                .filter(field -> field.getPiece() != null && field.getPiece().getPieceType().getName().equals(nameOfMovingPiece))
                .findFirst()
                .orElse(null);
        Field toField = fields.stream().filter(field -> field.getCode().equals(toCode)).findFirst().orElse(fromField);

        Grid grid = Grid.createGrid(fields, gridLogic).setKingField(kingFieldBeforeMove);

        try (MockedStatic<Grid> gridMockedStatic = Mockito.mockStatic(Grid.class)) {
            gridMockedStatic.when(() -> Grid.createGrid(grid, fromField, toField, gridLogic)).thenReturn(grid);
            when(QUEEN.getLogic(pieceTypeLogic)).thenReturn(queenLogic);
            when(queenLogic.isValidMove(any(Grid.class), any(Field.class), any(Field.class), eq(true))).thenReturn(expectedOutcome);

            assertEquals(expectedOutcome, checkLogic.isMovingIntoCheck(grid, fromField, toField, isOpponent, gridLogic));
        }
    }
}