package org.mark.chess.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.model.Field;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mark.chess.enums.Color.ATTACKING;
import static org.mark.chess.enums.Color.CHECKMATE;
import static org.mark.chess.enums.Color.STALEMATE;

@ExtendWith(MockitoExtension.class)
class BackgroundColorFactoryTest {

    @Test
    void testGetBackgroundColor_WhenAttacking_ReturnAttackingColor() {
        assertEquals(ATTACKING.getAwtColor(), BackgroundColorFactory.getBackgroundColor(new Field(null).setAttacking(true)));
    }

    @Test
    void testGetBackgroundColor_WhenCheckMate_ReturnCheckMateColor() {
        assertEquals(CHECKMATE.getAwtColor(), BackgroundColorFactory.getBackgroundColor(new Field(null).setCheckMate(true)));
    }

    @Test
    void testGetBackgroundColor_WhenStaleMate_ReturnStaleMateColor() {
        assertEquals(STALEMATE.getAwtColor(), BackgroundColorFactory.getBackgroundColor(new Field(null).setStaleMate(true)));
    }
}