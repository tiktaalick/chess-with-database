package org.mark.chess.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.Grid;
import org.mark.chess.move.Move;
import org.mark.chess.piece.Pawn;
import org.mark.chess.piece.PieceType;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class MoveTest {

    @InjectMocks
    private Move move = new Move(new Field(null));

    @Mock
    private Board board;

    @Mock
    private Button button;

    @Test
    void testDuringAMove() {
        Field field = new Field(null).setCode("d4");
        move.setFrom(field);
        move.setTo(new Field(null).setCode("d5"));

        assertTrue(move.isDuringAMove(field));
    }

    @Test
    void testIsFrom_WhenFieldWithNoPiece_ThenReturnFalse() {
        Game game = Game.create(board, WHITE);
        Field field = new Field(null);

        assertFalse(move.isFrom(game, field));
    }

    @Test
    void testIsFrom_WhenFieldWithWhitePawnAndItsBlacksTurn_ThenReturnFalse() {
        Game game = Game.create(board, WHITE).setCurrentPlayerColor(BLACK);
        Field field = new Field(new Pawn(WHITE));

        assertFalse(move.isFrom(game, field));
    }

    @Test
    void testIsFrom_WhenFieldWithWhitePawnAndItsWhitesTurn_ThenReturnTrue() {
        Game game = Game.create(board, WHITE).setCurrentPlayerColor(WHITE);
        Field field = new Field(new Pawn(WHITE));

        assertTrue(move.isFrom(game, field));
    }

    @Test
    void testSetTo() {
        Grid grid = Grid.create(board, WHITE);

        PieceType pieceType = new Pawn(WHITE);

        Field from = new Field(pieceType).setButton(button);
        Field to = new Field(null).setButton(button);

        move.setFrom(from);

        Move result = this.move.setTo(grid, to);
        assertEquals(to, result.getTo());
        assertEquals(pieceType, result.getTo().getPieceType());
    }
}
