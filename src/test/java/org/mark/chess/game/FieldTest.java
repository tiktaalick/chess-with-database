package org.mark.chess.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.piece.King;
import org.mark.chess.piece.Pawn;
import org.mark.chess.swing.Board;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.board.backgroundcolor.BackgroundColor.ATTACKING;
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

    @Test
    void testIsActivePlayerField_WhenActivePlayerField_ThenReturnTrue() {
        Game game = new Game(WHITE, grid);

        field.setId(FIELD_ID_C5).setPieceType(new Pawn(WHITE));

        assertTrue(field.isActivePlayerField(game));
    }

    @Test
    void testIsActivePlayerField_WhenEmptyField_ThenReturnFalse() {
        Game game = new Game(WHITE, grid);

        field.setId(FIELD_ID_C5);

        assertFalse(field.isActivePlayerField(game));
    }

    @Test
    void testIsActivePlayerField_WhenOpponentField_ThenReturnFalse() {
        Game game = new Game(WHITE, grid);

        field.setId(FIELD_ID_C5).setPieceType(new Pawn(BLACK));

        assertFalse(field.isActivePlayerField(game));
    }

    @Test
    void testIsNotAbleToMove_WhenNoMoves_ThenNotAbleToMove() {
        Game game = new Game(WHITE, grid).setCurrentPlayerColor(BLACK).setInProgress(true);

        field.setId(FIELD_ID_C5).setPieceType(new Pawn(BLACK));

        assertTrue(field.isNotAbleToMove(game, new ArrayList<>()));
    }

    @Test
    void testIsNotAbleToMove_WhenValidMoves_ThenAbleToMove() {
        Game game = new Game(WHITE, grid).setCurrentPlayerColor(BLACK).setInProgress(true);

        field.setId(FIELD_ID_C5).setPieceType(new Pawn(BLACK));

        List<Field> allValidMoves = List.of(field);

        assertFalse(field.isNotAbleToMove(game, allValidMoves));
    }

    @Test
    void testResetField() {
        field.setId(FIELD_ID_C5).setPieceType(new Pawn(BLACK));

        Field result = this.field.setPieceType(null);

        assertNull(result.getPieceType());
        assertEquals("c5", result.getCode());
    }

    @Test
    void testSetAttackingColors() {
        field.setId(FIELD_ID_C5).setPieceType(new Pawn(BLACK));
        Field kingField = new Field(new King(WHITE)).setId(Coordinates.createId("e1"));

        Grid grid = Grid.createEmpty().setFields(Arrays.asList(field, kingField));

        field.setAttackingColors(grid);

        assertTrue(field.isAttacking());
        assertTrue(kingField.isUnderAttack());
        assertEquals(ATTACKING.getAwtColor(), field.getBackgroundColor());
        assertEquals(ATTACKING.getAwtColor(), kingField.getBackgroundColor());
    }
}