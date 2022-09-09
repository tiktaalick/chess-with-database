package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Pawn;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mark.chess.enums.Color.WHITE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GridValueLogicTest {
    private static final int GRID_VALUE        = 9;
    private static final int HIGHER_FROM_VALUE = 10;
    private static final int MAX_SQUARE_ID     = 63;

    @InjectMocks
    private GridValueLogic gridValueLogic;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private Button button;

    @Test
    void testCreateAbsoluteFieldValues_WhenEarlierPossibleMovementIsHigher_ThenFromFieldGetEarlierValueAndToFieldGetsFirstPossibleGridValue() {
        Grid grid = Grid.createGrid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()),
                gridLogic);
        Grid gridAfterMovement = Grid
                .createGrid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()), gridLogic)
                .setGridValue(GRID_VALUE);

        Field from = new Field(new Pawn(WHITE)).setValue(HIGHER_FROM_VALUE);
        Field to = new Field(null);

        try (MockedStatic<Grid> gridMockedStatic = Mockito.mockStatic(Grid.class)) {
            gridMockedStatic.when(() -> Grid.createGrid(grid, from, to, gridLogic)).thenReturn(gridAfterMovement);

            gridValueLogic.createAbsoluteFieldValues(grid, from, to);

            assertEquals(HIGHER_FROM_VALUE, from.getValue());
            assertEquals(GRID_VALUE, to.getValue());
        }
    }

    @Test
    void testCreateAbsoluteFieldValues_WhenFirstPossibleMovement_ThenFromFieldAndToFieldGetFirstPossibleGridValue() {
        Grid grid = Grid.createGrid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()),
                gridLogic);
        Grid gridAfterMovement = Grid
                .createGrid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()), gridLogic)
                .setGridValue(GRID_VALUE);

        Field from = new Field(new Pawn(WHITE));
        Field to = new Field(null);

        try (MockedStatic<Grid> gridMockedStatic = Mockito.mockStatic(Grid.class)) {
            gridMockedStatic.when(() -> Grid.createGrid(grid, from, to, gridLogic)).thenReturn(gridAfterMovement);

            gridValueLogic.createAbsoluteFieldValues(grid, from, to);

            assertEquals(GRID_VALUE, from.getValue());
            assertEquals(GRID_VALUE, to.getValue());
        }
    }

    @Test
    void testCreateRelativeFieldValues() {
        Field from = new Field(new Pawn(WHITE)).setButton(button);

        List<Field> validMoves = List.of(new Field(null).setId(1).setValue(1).setButton(button),
                new Field(null).setId(3).setValue(3).setButton(button),
                new Field(null).setId(5).setValue(5).setButton(button),
                new Field(null).setId(9).setValue(9).setButton(button));

        try (MockedStatic<BackgroundColorFactory> backgroundColorFactoryMockedStatic = Mockito.mockStatic(BackgroundColorFactory.class)) {
            gridValueLogic.createRelativeFieldValues(validMoves, validMoves, from);

            backgroundColorFactoryMockedStatic.verify(() -> BackgroundColorFactory.getBackgroundColor(any(Field.class)), times(5));
            assertEquals(255, from.getRelativeValue());
            assertEquals(0, validMoves.get(0).getRelativeValue());
            assertEquals(63, validMoves.get(1).getRelativeValue());
            assertEquals(127, validMoves.get(2).getRelativeValue());
            assertEquals(255, validMoves.get(3).getRelativeValue());
        }
    }
}