package org.mark.chess.rulesengine.rule.piecetype;

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
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.BISHOP;
import static org.mark.chess.enums.PieceType.PAWN;

@ExtendWith(MockitoExtension.class)
class IsFriendlyFireRuleTest {
    private static final int LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private IsFriendlyFireRule isFriendlyFireRule;

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
    void testProcess_WhenFriendlyFire_ThenReturnTrue() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("a1");
        Field to = new Field(PAWN.createPiece(WHITE)).setCode("c3");

        fields.set(from.getId(), from);
        fields.set(to.getId(), to);

        Grid grid = Grid.createGrid(fields, gridLogic);

        assertTrue(isFriendlyFireRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, gridLogic, false)));
        assertFalse(isFriendlyFireRule.isValidMove());
    }

    @Test
    void testProcess_WhenNotFriendlyFire_ThenReturnFalse() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("a1");
        Field to = new Field(null).setCode("c3");

        fields.set(from.getId(), from);
        fields.set(to.getId(), to);

        Grid grid = Grid.createGrid(fields, gridLogic);

        assertFalse(isFriendlyFireRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, gridLogic, false)));
        assertTrue(isFriendlyFireRule.isValidMove());
    }
}