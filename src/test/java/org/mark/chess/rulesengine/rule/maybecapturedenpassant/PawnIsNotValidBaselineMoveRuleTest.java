package org.mark.chess.rulesengine.rule.maybecapturedenpassant;

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
class PawnIsNotValidBaselineMoveRuleTest {
    private static final int LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private PawnIsNotValidBaselineMoveRule pawnIsNotValidBaselineMoveRule;

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
    void testProcess_WhenIsNotBaselineMove_ThenReturnTrue() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("e2");
        Field to = new Field(PAWN.createPiece(WHITE)).setCode("e3");

        fields.set(from.getId(), from);
        fields.set(to.getId(), to);

        Grid grid = Grid.createGrid(fields, gridLogic);

        assertTrue(pawnIsNotValidBaselineMoveRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, gridLogic, false)));
        assertFalse(pawnIsNotValidBaselineMoveRule.create());
    }

    @Test
    void testProcess_WhenNotFriendlyFire_ThenReturnFalse() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("e2");
        Field to = new Field(PAWN.createPiece(WHITE)).setCode("e4");

        fields.set(from.getId(), from);
        fields.set(to.getId(), to);

        Grid grid = Grid.createGrid(fields, gridLogic);

        assertFalse(pawnIsNotValidBaselineMoveRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, gridLogic, false)));
    }
}