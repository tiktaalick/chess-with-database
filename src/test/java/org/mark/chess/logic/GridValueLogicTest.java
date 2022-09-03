package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Pawn;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mark.chess.enums.Color.WHITE;

@ExtendWith(MockitoExtension.class)
class GridValueLogicTest {
    private static final int            GRID_VALUE        = 9;
    private static final int            HIGHER_FROM_VALUE = 10;
    private static final int            MAX_SQUARE_ID     = 63;
    @InjectMocks
    private              GridValueLogic gridValueLogic;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private FieldLogic fieldLogic;

    @Test
    void testCreateAbsoluteFieldValues_WhenEarlierPossibleMovementIsHigher_ThenFromFieldGetEarlierValueAndToFieldGetsFirstPossibleGridValue() {
        Grid grid = Grid.createGrid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()),
                gridLogic,
                fieldLogic);
        Grid gridAfterMovement = Grid
                .createGrid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()),
                        gridLogic,
                        fieldLogic)
                .setGridValue(GRID_VALUE);

        Field from = new Field(new Pawn(WHITE)).setValue(HIGHER_FROM_VALUE);
        Field to = new Field(null);

        try (MockedStatic<Grid> gridMockedStatic = Mockito.mockStatic(Grid.class)) {
            gridMockedStatic.when(() -> Grid.createGrid(grid, from, to, gridLogic, fieldLogic)).thenReturn(gridAfterMovement);

            gridValueLogic.createAbsoluteFieldValues(grid, from, to);

            assertEquals(HIGHER_FROM_VALUE, from.getValue());
            assertEquals(GRID_VALUE, to.getValue());
        }
    }

    @Test
    void testCreateAbsoluteFieldValues_WhenFirstPossibleMovement_ThenFromFieldAndToFieldGetFirstPossibleGridValue() {
        Grid grid = Grid.createGrid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()),
                gridLogic,
                fieldLogic);
        Grid gridAfterMovement = Grid
                .createGrid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()),
                        gridLogic,
                        fieldLogic)
                .setGridValue(GRID_VALUE);

        Field from = new Field(new Pawn(WHITE));
        Field to = new Field(null);

        try (MockedStatic<Grid> gridMockedStatic = Mockito.mockStatic(Grid.class)) {
            gridMockedStatic.when(() -> Grid.createGrid(grid, from, to, gridLogic, fieldLogic)).thenReturn(gridAfterMovement);

            gridValueLogic.createAbsoluteFieldValues(grid, from, to);

            assertEquals(GRID_VALUE, from.getValue());
            assertEquals(GRID_VALUE, to.getValue());
        }
    }

    @Test
    void testCreateRelativeFieldValues() {

    }
}