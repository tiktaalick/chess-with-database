package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mark.chess.enums.PieceType.PAWN;

@ExtendWith(MockitoExtension.class)
class IsJumpingRuleTest {
    private static final int LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private IsJumpingRule isJumpingRule;

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
    void testProcess_WhenJumping_ThenReturnTrue() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("a1");
        Field to = new Field(null).setCode("c3");
        Field opponent = new Field(PAWN.createPiece(BLACK)).setCode("b2");

        fields.set(from.getId(), from);
        fields.set(opponent.getId(), opponent);

        Grid grid = new Grid(fields);

        assertTrue(isJumpingRule.test(new IsValidMoveParameter(grid, from, to, false)));
        assertFalse(isJumpingRule.create());
    }

    @Test
    void testProcess_WhenNotJumping_ThenReturnFalse() {
        Field from = new Field(BISHOP.createPiece(WHITE)).setCode("a1");
        Field to = new Field(null).setCode("c3");
        Field opponent = new Field(PAWN.createPiece(BLACK)).setCode("d4");

        fields.set(from.getId(), from);
        fields.set(opponent.getId(), opponent);

        Grid grid = new Grid(fields);

        assertFalse(isJumpingRule.test(new IsValidMoveParameter(grid, from, to, false)));
    }
}