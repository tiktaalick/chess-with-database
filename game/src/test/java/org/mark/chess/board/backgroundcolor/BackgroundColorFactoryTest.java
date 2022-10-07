package org.mark.chess.board.backgroundcolor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.board.Field;
import org.mark.chess.board.backgroundcolor.BackgroundColorRulesEngine;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mark.chess.board.backgroundcolor.BackgroundColor.ATTACKING;
import static org.mark.chess.board.backgroundcolor.BackgroundColor.CHECKMATE;
import static org.mark.chess.board.backgroundcolor.BackgroundColor.STALEMATE;

@ExtendWith(MockitoExtension.class)
class BackgroundColorFactoryTest {
    private static final BackgroundColorRulesEngine backgroundColorRulesEngine = new BackgroundColorRulesEngine();

    @Test
    void testGetBackgroundColor_WhenAttacking_ReturnAttackingColor() {
        assertEquals(ATTACKING.getAwtColor(), backgroundColorRulesEngine.process(new Field(null).setAttacking(true)));
    }

    @Test
    void testGetBackgroundColor_WhenCheckMate_ReturnCheckMateColor() {
        assertEquals(CHECKMATE.getAwtColor(), backgroundColorRulesEngine.process(new Field(null).setCheckMate(true)));
    }

    @Test
    void testGetBackgroundColor_WhenStaleMate_ReturnStaleMateColor() {
        assertEquals(STALEMATE.getAwtColor(), backgroundColorRulesEngine.process(new Field(null).setStaleMate(true)));
    }
}