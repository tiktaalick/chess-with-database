package org.mark.chess.piece.isvalidmove;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.board.Field;
import org.mark.chess.board.Chessboard;
import org.mark.chess.piece.Bishop;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.player.PlayerColor.WHITE;

@ExtendWith(MockitoExtension.class)
class KingIsValidBasicMoveRuleTest {

    private static final char DELIMITER = ';';

    @InjectMocks
    private KingIsValidBasicMoveRule kingIsValidBasicMoveRule;

    @Test
    void testRule_WhenInvalidBasicMove_ThenReturnFalse() {
        Field from = new Field(new Bishop(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("e5");

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);

        assertFalse(kingIsValidBasicMoveRule.hasResult(new IsValidMoveParameter(chessboard, from, to, false)));
    }

    @ParameterizedTest
    @CsvSource(value = {"e3;d4", "e3;e4", "e3;f4", "e3;e2", "e3;e4", "e3;d2", "e3;d3", "e3;d4"}, delimiter = DELIMITER)
    void testRule_WhenValidBasicMove_ThenReturnTrue(String codeFrom, String codeTo) {
        Field from = new Field(new Bishop(WHITE)).setCode(codeFrom);
        Field to = new Field(null).setCode(codeTo);

        Chessboard chessboard = Chessboard.createEmpty();
        chessboard.getFields().set(from.getId(), from);

        assertTrue(kingIsValidBasicMoveRule.hasResult(new IsValidMoveParameter(chessboard, from, to, false)));
        assertTrue(kingIsValidBasicMoveRule.createResult());
    }
}