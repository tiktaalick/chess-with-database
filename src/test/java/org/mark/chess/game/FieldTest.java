package org.mark.chess.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Grid;
import org.mark.chess.board.Field;
import org.mark.chess.piece.Pawn;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

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
        Game game = new Game(WHITE, grid);

        field.initialize(board, FIELD_ID_C5).setPieceType(new Pawn(WHITE));

        assertTrue(field.isActivePlayerField(game));
    }

    @Test
    void testIsActivePlayerField_WhenEmptyField_ThenReturnFalse() {
        Game game = new Game(WHITE, grid);

        field.initialize(board, FIELD_ID_C5);

        assertFalse(field.isActivePlayerField(game));
    }

    @Test
    void testIsActivePlayerField_WhenOpponentField_ThenReturnFalse() {
        Game game = new Game(WHITE, grid);

        field.initialize(board, FIELD_ID_C5).setPieceType(new Pawn(BLACK));

        assertFalse(field.isActivePlayerField(game));
    }
}