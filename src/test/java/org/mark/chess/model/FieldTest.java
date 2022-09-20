package org.mark.chess.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;

@ExtendWith(MockitoExtension.class)
class FieldTest {
    private static final int FIELD_ID_C5 = 26;

    @InjectMocks
    private Field field;

    @Mock
    private Board board;

    @Mock
    private Grid grid;

    @Mock
    private Button button;

    @Test
    void testIsActivePlayerField_WhenActivePlayerField_ThenReturnTrue() {
        Game game = new Game(true, WHITE, grid);

        field.initialize(board, FIELD_ID_C5).setPiece(new Pawn(WHITE));

        assertTrue(field.isActivePlayerField(game));
    }

    @Test
    void testIsActivePlayerField_WhenEmptyField_ThenReturnFalse() {
        Game game = new Game(true, WHITE, grid);

        field.initialize(board, FIELD_ID_C5);

        assertFalse(field.isActivePlayerField(game));
    }

    @Test
    void testIsActivePlayerField_WhenOpponentField_ThenReturnFalse() {
        Game game = new Game(true, WHITE, grid);

        field.initialize(board, FIELD_ID_C5).setPiece(new Pawn(BLACK));

        assertFalse(field.isActivePlayerField(game));
    }

    @Test
    void testSetAttacking() {
        Grid grid = Grid.create(board, WHITE);

        try (MockedStatic<BackgroundColorFactory> backgroundColorFactoryMockedStatic = Mockito.mockStatic(BackgroundColorFactory.class)) {
            field.setButton(button).setAttacking(grid);

            backgroundColorFactoryMockedStatic.verify(() -> BackgroundColorFactory.getBackgroundColor(field));
            backgroundColorFactoryMockedStatic.verify(() -> BackgroundColorFactory.getBackgroundColor(grid.getOpponentKingField()));

            assertTrue(field.isAttacking());
        }
    }

    @Test
    void testSetValidMove() {
        try (MockedStatic<BackgroundColorFactory> backgroundColorFactoryMockedStatic = Mockito.mockStatic(BackgroundColorFactory.class)) {
            field.setButton(button).setValidMove(true);

            backgroundColorFactoryMockedStatic.verify(() -> BackgroundColorFactory.getBackgroundColor(field));
            assertTrue(field.isValidMove());
        }
    }
}