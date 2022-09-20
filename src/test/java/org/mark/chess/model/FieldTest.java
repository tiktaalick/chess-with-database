package org.mark.chess.model;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FieldTest {
//    private static final int FIELD_ID_C4                = 34;
//    private static final int FIELD_ID_C5                = 26;
//    private static final int NUMBER_OF_COLUMNS_AND_ROWS = 8;
//
//    @InjectMocks
//    private Field field;
//
//    @Mock
//    private Board board;
//
//    @Test
//    void testInitializeField_WhenDark_ThenReturnDarkField() {
//        field.initialize(board, FIELD_ID_C4);
//
//        assertEquals("c4", field.getCode());
//        assertEquals(3 - 1 + (NUMBER_OF_COLUMNS_AND_ROWS - 4) * NUMBER_OF_COLUMNS_AND_ROWS, field.getId());
//        assertEquals(3, field.getCoordinates().getX());
//        assertEquals(4, field.getCoordinates().getY());
//        assertEquals(3 * FIELD_WIDTH_AND_HEIGHT, field.getButton().getX());
//        assertEquals(4 * FIELD_WIDTH_AND_HEIGHT, field.getButton().getY());
//        assertEquals(Color.LIGHT.getAwtColor(), field.getButton().getBackground());
//    }
//
//    @Test
//    void testInitializeField_WhenLight_ThenReturnLightField() {
//        field.initialize(board, FIELD_ID_C5);
//
//        assertEquals("c5", field.getCode());
//        assertEquals(3 - 1 + (NUMBER_OF_COLUMNS_AND_ROWS - 5) * NUMBER_OF_COLUMNS_AND_ROWS, field.getId());
//        assertEquals(3, field.getCoordinates().getX());
//        assertEquals(5, field.getCoordinates().getY());
//        assertEquals(3 * FIELD_WIDTH_AND_HEIGHT, field.getButton().getX());
//        assertEquals(5 * FIELD_WIDTH_AND_HEIGHT, field.getButton().getY());
//        assertEquals(Color.DARK.getAwtColor(), field.getButton().getBackground());
//    }
//
//    @Test
//    void testIsActivePlayerField_WhenActivePlayerField_ThenReturnTrue() {
//        Game game = new Game().setPlayers(Arrays.asList(new Human().setColor(WHITE), new Human().setColor(BLACK))).setCurrentPlayerColor(WHITE);
//
//        field.initialize(board, FIELD_ID_C5).setPiece(new Pawn(WHITE));
//
//        assertTrue(field.isActivePlayerField(game));
//    }
//
//    @Test
//    void testIsActivePlayerField_WhenEmptyField_ThenReturnFalse() {
//        Game game = new Game().setPlayers(Arrays.asList(new Human().setColor(WHITE), new Human().setColor(BLACK))).setCurrentPlayerColor(WHITE);
//
//        field.initialize(board, FIELD_ID_C5);
//
//        assertFalse(field.isActivePlayerField(game));
//    }
//
//    @Test
//    void testIsActivePlayerField_WhenOpponentField_ThenReturnFalse() {
//        Game game = new Game().setPlayers(Arrays.asList(new Human().setColor(WHITE), new Human().setColor(BLACK))).setCurrentPlayerColor(WHITE);
//
//        field.initialize(board, FIELD_ID_C5).setPiece(new Pawn(BLACK));
//
//        assertFalse(field.isActivePlayerField(game));
//    }
}