package org.mark.chess.logic;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GridValueLogicTest {
    private static final int GRID_VALUE        = 9;
    private static final int HIGHER_FROM_VALUE = 10;
    private static final int MAX_SQUARE_ID     = 63;

//    @InjectMocks
//    private GridValueLogic gridValueLogic;
//
//    @Mock
//    private Button button;
//
//    @Mock
//    private MoveLogic moveLogic;

//    @Test
//    void testCreateAbsoluteFieldValues_WhenEarlierPossibleMovementIsHigher_ThenFromFieldGetEarlierValueAndToFieldGetsFirstPossibleGridValue() {
//        Grid grid = new Grid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()),
//                checkLogic,
//                moveLogic);
//        Grid gridAfterMovement = new Grid(IntStream
//                .rangeClosed(0, MAX_SQUARE_ID)
//                .mapToObj(id -> new Field(null).setId(id))
//                .collect(Collectors.toList()), checkLogic, moveLogic).setGridValue(GRID_VALUE);
//
//        Field from = new Field(new Pawn(WHITE)).setValue(HIGHER_FROM_VALUE);
//        Field to = new Field(null);
//
//        try (MockedStatic<Grid> gridMockedStatic = Mockito.mockStatic(Grid.class)) {
//            gridMockedStatic.when(() -> grid.createGrid(grid, from, to)).thenReturn(gridAfterMovement);
//
//            gridValueLogic.createAbsoluteFieldValues(grid, from, to);
//
//            assertEquals(HIGHER_FROM_VALUE, from.getValue());
//            assertEquals(GRID_VALUE, to.getValue());
//        }
//    }

//    @Test
//    void testCreateAbsoluteFieldValues_WhenFirstPossibleMovement_ThenFromFieldAndToFieldGetFirstPossibleGridValue() {
//        Grid grid = new Grid(IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()),
//                checkLogic,
//                moveLogic);
//        Grid gridAfterMovement = new Grid(IntStream
//                .rangeClosed(0, MAX_SQUARE_ID)
//                .mapToObj(id -> new Field(null).setId(id))
//                .collect(Collectors.toList()), checkLogic, moveLogic).setGridValue(GRID_VALUE);
//
//        Field from = new Field(new Pawn(WHITE));
//        Field to = new Field(null);
//
//        try (MockedStatic<Grid> gridMockedStatic = Mockito.mockStatic(Grid.class)) {
//            gridMockedStatic.when(() -> grid.createGrid(grid, from, to)).thenReturn(gridAfterMovement);
//
//            gridValueLogic.createAbsoluteFieldValues(grid, from, to);
//
//            assertEquals(GRID_VALUE, from.getValue());
//            assertEquals(GRID_VALUE, to.getValue());
//        }
//    }

//    @Test
//    void testCreateRelativeFieldValues() {
//        Field from = new Field(new Pawn(WHITE)).setButton(button);
//
//        List<Field> validMoves = List.of(new Field(null).setId(1).setValue(1).setButton(button),
//                new Field(null).setId(3).setValue(3).setButton(button),
//                new Field(null).setId(5).setValue(5).setButton(button),
//                new Field(null).setId(9).setValue(9).setButton(button));
//
//        try (MockedStatic<BackgroundColorFactory> backgroundColorFactoryMockedStatic = Mockito.mockStatic(BackgroundColorFactory.class)) {
////            game.createRelativeFieldValues(validMoves, validMoves, from);
//
//            backgroundColorFactoryMockedStatic.verify(() -> BackgroundColorFactory.getBackgroundColor(any(Field.class)), times(5));
//            assertEquals(255, from.getRelativeValue());
//            assertEquals(0, validMoves.get(0).getRelativeValue());
//            assertEquals(63, validMoves.get(1).getRelativeValue());
//            assertEquals(127, validMoves.get(2).getRelativeValue());
//            assertEquals(255, validMoves.get(3).getRelativeValue());
//        }
//    }
}