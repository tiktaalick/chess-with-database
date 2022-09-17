package org.mark.chess.rulesengine.rule.piecetype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.logic.CheckLogic;
import org.mark.chess.logic.GridLogic;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.KNIGHT;

@ExtendWith(MockitoExtension.class)
class KnightIsValidBasicMoveRuleTest {
    private static final char DELIMITER                   = ';';
    private static final int  LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private KnightIsValidBasicMoveRule knightIsValidBasicMoveRule;

    @Mock
    private CheckLogic checkLogic;

    @Mock
    private GridLogic gridLogic;

    @Mock
    private Button button;

    private List<Field> fields;

    @BeforeEach
    void beforeEach() {
        fields = IntStream.rangeClosed(0, LAST_SQUARE_ON_THE_BOARD_ID).mapToObj(id -> {
            Field field = new Field(null).setId(id).setValidMove(false);
            return field.setButton(button);
        }).collect(Collectors.toList());
    }

    @Test
    void testRule_WhenInvalidBasicMove_ThenReturnFalse() {
        Field from = new Field(KNIGHT.createPiece(WHITE)).setCode("a1");
        Field to = new Field(null).setCode("b2");

        fields.set(from.getId(), from);

        Grid grid = Grid.createGrid(fields, gridLogic);

        assertFalse(knightIsValidBasicMoveRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, gridLogic, false)));
        assertFalse(knightIsValidBasicMoveRule.isValidMove());
    }

    @ParameterizedTest
    @CsvSource(value = {"e3;d5", "e3;g2"}, delimiter = DELIMITER)
    void testRule_WhenValidBasicMove_ThenReturnTrue(String codeFrom, String codeTo) {
        Field from = new Field(KNIGHT.createPiece(WHITE)).setCode(codeFrom);
        Field to = new Field(null).setCode(codeTo);

        fields.set(from.getId(), from);

        Grid grid = Grid.createGrid(fields, gridLogic);

        assertTrue(knightIsValidBasicMoveRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, gridLogic, false)));
        assertTrue(knightIsValidBasicMoveRule.isValidMove());
    }
}