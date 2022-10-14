package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.board.Field;
import org.mark.chess.board.Chessboard;
import org.mark.chess.piece.King;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class KnightIsValidBasicMoveRuleTest {

    private static final char DELIMITER = ';';

    @InjectMocks
    private KnightIsValidBasicMoveRule knightIsValidBasicMoveRule;

    @Test
    void testRule_WhenInvalidBasicMove_ThenReturnFalse() {
        Field from = new Field(new King(WHITE)).setCode("a1");
        Field to = new Field(null).setCode("b2");

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);

        assertFalse(knightIsValidBasicMoveRule.isApplicable(new IsValidMoveParameter(chessboard, from, to, false)));
    }

    @ParameterizedTest
    @CsvSource(value = {"e3;d5", "e3;g2"}, delimiter = DELIMITER)
    void testRule_WhenValidBasicMove_ThenReturnTrue(String codeFrom, String codeTo) {
        Field from = new Field(new King(WHITE)).setCode(codeFrom);
        Field to = new Field(null).setCode(codeTo);

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);

        assertTrue(knightIsValidBasicMoveRule.isApplicable(new IsValidMoveParameter(chessboard, from, to, false)));
        assertTrue(knightIsValidBasicMoveRule.create());
    }
}