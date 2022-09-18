package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.logic.CheckLogic;
import org.mark.chess.logic.MoveLogic;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.Pawn;
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
import static org.mark.chess.enums.PieceType.PAWN;

@ExtendWith(MockitoExtension.class)
class PawnIsValidEnPassantMoveRuleTest {
    private static final int LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private PawnIsValidEnPassantMoveRule pawnIsValidEnPassantMoveRule;

    @Mock
    private CheckLogic checkLogic;

    @Mock
    private Button button;

    @Mock
    private MoveLogic moveLogic;

    private List<Field> fields;

    @BeforeEach
    void beforeEach() {
        fields = IntStream.rangeClosed(0, LAST_SQUARE_ON_THE_BOARD_ID).mapToObj(id -> {
            Field field = new Field(null).setId(id).setValidMove(false);
            return field.setButton(button);
        }).collect(Collectors.toList());
    }

    @Test
    void testRule_WhenInvalidEnPassantMove_ThenReturnFalse() {
        Field from = new Field(PAWN.createPiece(WHITE)).setCode("e5");
        Field to = new Field(null).setCode("d6");
        Field opponentField = new Field(PAWN.createPiece(BLACK)).setCode("d5");

        fields.set(from.getId(), from);
        fields.set(to.getId(), to);
        fields.set(opponentField.getId(), opponentField);

        Grid grid = new Grid(fields, checkLogic, moveLogic);

        assertFalse(pawnIsValidEnPassantMoveRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, false)));
    }

    @Test
    void testRule_WhenValidEnPassantMove_ThenReturnTrue() {
        Field from = new Field(PAWN.createPiece(WHITE)).setCode("e5");
        Field to = new Field(null).setCode("d6");
        Field opponentField = new Field(((Pawn) PAWN.createPiece(BLACK)).setMayBeCapturedEnPassant(true)).setCode("d5");

        fields.set(from.getId(), from);
        fields.set(to.getId(), to);
        fields.set(opponentField.getId(), opponentField);

        Grid grid = new Grid(fields, checkLogic, moveLogic);

        assertTrue(pawnIsValidEnPassantMoveRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, false)));
        assertTrue(pawnIsValidEnPassantMoveRule.create());
    }
}