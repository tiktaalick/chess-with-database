package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.BISHOP;
import static org.mark.chess.enums.PieceType.KING;
import static org.mark.chess.enums.PieceType.QUEEN;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IsMovingIntoCheckRuleTest {
    private static final int LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private IsMovingIntoCheckRule isMovingIntoCheckRule;

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
    void testProcess_WhenMovingIntoCheck_ThenReturnTrue() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("c1");
        Field opponentField = new Field(QUEEN.createPiece(BLACK)).setCode("e4");
        Field kingField = new Field(KING.createPiece(WHITE)).setCode("e1");

        fields.set(from.getId(), from);
        fields.set(opponentField.getId(), opponentField);
        fields.set(kingField.getId(), kingField);

        Grid grid = Grid.createGrid(fields, gridLogic);

        when(checkLogic.isMovingIntoCheck(grid, from, to, false, gridLogic)).thenReturn(true);

        assertTrue(isMovingIntoCheckRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, gridLogic, false)));
        assertFalse(isMovingIntoCheckRule.create());
    }

    @Test
    void testProcess_WhenNotMovingIntoCheck_ThenReturnFalse() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("e3");
        Field to = new Field(null).setCode("c1");
        Field opponentField = new Field(QUEEN.createPiece(BLACK)).setCode("e4");
        Field kingField = new Field(KING.createPiece(WHITE)).setCode("e1");

        fields.set(from.getId(), from);
        fields.set(opponentField.getId(), opponentField);
        fields.set(kingField.getId(), kingField);

        Grid grid = Grid.createGrid(fields, gridLogic);

        when(checkLogic.isMovingIntoCheck(grid, from, to, false, gridLogic)).thenReturn(false);

        assertFalse(isMovingIntoCheckRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, gridLogic, false)));
    }
}