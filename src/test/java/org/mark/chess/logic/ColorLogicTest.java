package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Grid;
import org.mark.chess.model.King;
import org.mark.chess.model.Queen;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.ATTACKING;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ColorLogicTest {
    private static final int HIGH_VALUE       = 99999;
    private static final int MAX_SQUARE_ID    = 63;
    private static final int NUMBER_OF_FIELDS = 64;

    @InjectMocks
    private ColorLogic colorLogic;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private Button button;

    @Mock
    private FieldLogic fieldLogic;

    @Mock
    private GameLogic gameLogic;

    @Mock
    private GridValueLogic gridValueLogic;

    @Test
    void testSetAttacking() {
        List<Field> fields = IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> {
            Field field = new Field(null).setId(id);
            return field.setButton(button);
        }).collect(Collectors.toList());

        Grid grid = Grid.createGrid(fields, gridLogic, fieldLogic);

        Field opponentKingField = fields
                .stream()
                .filter(field -> field.getCode().equals("e8"))
                .findFirst()
                .orElse(new Field(null))
                .setPiece(new King(BLACK));
        fields.set(opponentKingField.getId(), opponentKingField);
        grid.setOpponentKingField(opponentKingField);

        Field queenField = fields
                .stream()
                .filter(field -> field.getCode().equals("e4"))
                .findFirst()
                .orElse(new Field(null))
                .setPiece(new Queen(WHITE));
        fields.set(queenField.getId(), queenField);

        try (MockedStatic<BackgroundColorFactory> backgroundColorFactoryMockedStatic = Mockito.mockStatic(BackgroundColorFactory.class)) {

            backgroundColorFactoryMockedStatic
                    .when(() -> BackgroundColorFactory.getBackgroundColor(any(Field.class)))
                    .thenReturn(ATTACKING.getAwtColor());

            colorLogic.setAttacking(grid, queenField);

            assertTrue(queenField.isAttacking());
            assertTrue(grid.getOpponentKingField().isUnderAttack());
            verify(button, times(2)).setBackground(ATTACKING.getAwtColor());
        }
    }

    @Test
    void testSetKingFieldColors() {
        List<Field> fields = IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> {
            Field field = new Field(null).setId(id);
            return field.setButton(button);
        }).collect(Collectors.toList());

        Grid grid = Grid.createGrid(fields, gridLogic, fieldLogic);
        Game game = new Game().setGrid(grid);

        Field opponentKingField = fields
                .stream()
                .filter(field -> field.getCode().equals("e8"))
                .findFirst()
                .orElse(new Field(null))
                .setPiece(new King(BLACK));
        fields.set(opponentKingField.getId(), opponentKingField);
        grid.setOpponentKingField(opponentKingField);

        Field queenField = fields
                .stream()
                .filter(field -> field.getCode().equals("e4"))
                .findFirst()
                .orElse(new Field(null))
                .setPiece(new Queen(WHITE));
        fields.set(queenField.getId(), queenField);

        Collection<Field> allValidMoves = new ArrayList<>();

        colorLogic.setKingFieldColors(game, allValidMoves);

        verify(gridLogic).setKingFieldFlags(game, allValidMoves, opponentKingField);
        verify(gridLogic, never()).setKingFieldFlags(game, allValidMoves, queenField);
        verify(gameLogic).setGameProgress(game, opponentKingField);
        verify(gameLogic, never()).setGameProgress(game, queenField);
        verify(button, times(2)).setBackground(any(java.awt.Color.class));
    }

    @Test
    void testSetValidMoveColors() {
        List<Field> fields = IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> {
            Field field = new Field(null).setId(id).setValue(HIGH_VALUE).setRelativeValue(HIGH_VALUE);
            return field.setButton(button);
        }).collect(Collectors.toList());

        Grid grid = Grid.createGrid(fields, gridLogic, fieldLogic);
        Field from = new Field(null);

        Iterable<Field> validMoves = new ArrayList<>();
        Collection<Field> allValidMoves = List.of(new Field(null).setId(8), new Field(null).setId(9), new Field(null).setId(10));

        colorLogic.setValidMoveColors(grid, from, validMoves, allValidMoves);

        assertEquals(NUMBER_OF_FIELDS, grid.getFields().stream().filter(field -> field.getValue() == null).count());
        assertEquals(NUMBER_OF_FIELDS, grid.getFields().stream().filter(field -> field.getRelativeValue() == null).count());
        verify(gridValueLogic, times(3)).createAbsoluteFieldValues(eq(grid), eq(from), any(Field.class));
        verify(gridValueLogic).createRelativeFieldValues(validMoves, allValidMoves, from);
    }
}