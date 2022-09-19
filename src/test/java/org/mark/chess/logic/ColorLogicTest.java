package org.mark.chess.logic;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ColorLogicTest {
//    private static final int HIGH_VALUE       = 99999;
//    private static final int MAX_SQUARE_ID    = 63;
//    private static final int NUMBER_OF_FIELDS = 64;
//
//    @InjectMocks
//    private ColorLogic colorLogic;
//
//    @Mock
//    private Button button;
//
//    @Mock
//    private GameLogic gameLogic;
//
//    @Mock
//    private MoveLogic moveLogic;
//
//    @Test
//    void testSetAttacking() {
//        List<Field> fields = IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> {
//            Field field = new Field(null).setId(id);
//            return field.setButton(button);
//        }).collect(Collectors.toList());
//
//        Grid grid = new Grid(fields);
//
//        Field opponentKingField = fields
//                .stream()
//                .filter(field -> field.getCode().equals("e8"))
//                .findFirst()
//                .orElse(new Field(null))
//                .setPiece(new King(BLACK));
//        fields.set(opponentKingField.getId(), opponentKingField);
//        grid.setOpponentKingField(opponentKingField);
//
//        Field queenField = fields
//                .stream()
//                .filter(field -> field.getCode().equals("e4"))
//                .findFirst()
//                .orElse(new Field(null))
//                .setPiece(new Queen(WHITE));
//        fields.set(queenField.getId(), queenField);
//
//        try (MockedStatic<BackgroundColorFactory> backgroundColorFactoryMockedStatic = Mockito.mockStatic(BackgroundColorFactory.class)) {
//
//            backgroundColorFactoryMockedStatic
//                    .when(() -> BackgroundColorFactory.getBackgroundColor(any(Field.class)))
//                    .thenReturn(ATTACKING.getAwtColor());
//
//            queenField.setAttacking(grid);
//
//            assertTrue(queenField.isAttacking());
//            assertTrue(grid.getOpponentKingField().isUnderAttack());
//            verify(button, times(2)).setBackground(ATTACKING.getAwtColor());
//        }
//    }
//
////    @Test
////    void testSetKingFieldColors() {
////        List<Field> fields = IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> {
////            Field field = new Field(null).setId(id);
////            return field.setButton(button);
////        }).collect(Collectors.toList());
////
////        Grid grid = new Grid(fields, checkLogic, moveLogic);
////        Game game = new Game().setGrid(grid);
////
////        Field opponentKingField = fields
////                .stream()
////                .filter(field -> field.getCode().equals("e8"))
////                .findFirst()
////                .orElse(new Field(null))
////                .setPiece(new King(BLACK));
////        fields.set(opponentKingField.getId(), opponentKingField);
////        grid.setOpponentKingField(opponentKingField);
////
////        Field queenField = fields
////                .stream()
////                .filter(field -> field.getCode().equals("e4"))
////                .findFirst()
////                .orElse(new Field(null))
////                .setPiece(new Queen(WHITE));
////        fields.set(queenField.getId(), queenField);
////
////        Collection<Field> allValidMoves = new ArrayList<>();
////
////        colorLogic.setKingFieldColors(game, allValidMoves);
////
////        verify(grid).setKingFieldFlags(game, allValidMoves, opponentKingField);
////        verify(grid, never()).setKingFieldFlags(game, allValidMoves, queenField);
////        verify(gameLogic).setGameProgress(game, opponentKingField);
////        verify(gameLogic, never()).setGameProgress(game, queenField);
////        verify(button, times(2)).setBackground(any(java.awt.Color.class));
////    }
//
//    @Test
//    void testSetValidMoveColors() {
//        List<Field> fields = IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> {
//            Field field = new Field(null).setId(id).setValue(HIGH_VALUE).setRelativeValue(HIGH_VALUE);
//            return field.setButton(button);
//        }).collect(Collectors.toList());
//
//        Grid grid = new Grid(fields);
//        Field from = new Field(null);
//
//        Collection<Field> validMoves = new ArrayList<>();
//        Collection<Field> allValidMoves = List.of(new Field(null).setId(8), new Field(null).setId(9), new Field(null).setId(10));
//
////        game.setValidMoveColors(grid, from, validMoves, allValidMoves);
//
//        assertEquals(NUMBER_OF_FIELDS, grid.getFields().stream().filter(field -> field.getValue() == null).count());
//        assertEquals(NUMBER_OF_FIELDS, grid.getFields().stream().filter(field -> field.getRelativeValue() == null).count());
////        verify(game, times(3)).createAbsoluteFieldValues(eq(grid), eq(from), any(Field.class));
////        verify(game).createRelativeFieldValues(validMoves, allValidMoves, from);
//    }
}