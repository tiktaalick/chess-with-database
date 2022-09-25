package org.mark.chess.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Coordinates;
import org.mark.chess.board.Field;
import org.mark.chess.piece.Pawn;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class PawnTest {
    @InjectMocks
    private Pawn pawn;

    @Test
    void testIsPawnBeingPromoted_WhenBlackPawnEndsAtTheLastRow_ThenReturnTrue() {
        Field from = new Field(new Pawn(BLACK)).setCoordinates(new Coordinates(6, 6));
        Field to = new Field(new Pawn(BLACK)).setCoordinates(new Coordinates(4, 1));

        assertTrue(pawn.setPawnBeingPromoted(from, to).isPawnBeingPromoted());
    }

    @Test
    void testIsPawnBeingPromoted_WhenBlackPawnStartsAtTheLastRow_ThenReturnTrue() {
        Field from = new Field(new Pawn(BLACK)).setCoordinates(new Coordinates(4, 1));
        Field to = new Field(new Pawn(BLACK)).setCoordinates(new Coordinates(6, 6));

        assertTrue(pawn.setPawnBeingPromoted(from, to).isPawnBeingPromoted());
    }

    @Test
    void testIsPawnBeingPromoted_WhenPawnBeingPromoted_ThenReturnTrue() {
        Field from = new Field(new Pawn(WHITE).setPawnBeingPromoted(true));
        Field to = new Field(null);

        assertTrue(pawn.setPawnBeingPromoted(from, to).isPawnBeingPromoted());
    }

    @Test
    void testIsPawnBeingPromoted_WhenWhitePawnEndsAtTheLastRow_ThenReturnTrue() {
        Field from = new Field(new Pawn(WHITE)).setCoordinates(new Coordinates(6, 6));
        Field to = new Field(new Pawn(WHITE)).setCoordinates(new Coordinates(4, 8));

        assertTrue(pawn.setPawnBeingPromoted(from, to).isPawnBeingPromoted());
    }

    @Test
    void testIsPawnBeingPromoted_WhenWhitePawnStartsAtTheLastRow_ThenReturnTrue() {
        Field from = new Field(new Pawn(WHITE)).setCoordinates(new Coordinates(4, 8));
        Field to = new Field(new Pawn(BLACK)).setCoordinates(new Coordinates(6, 6));

        assertTrue(pawn.setPawnBeingPromoted(from, to).isPawnBeingPromoted());
    }
}