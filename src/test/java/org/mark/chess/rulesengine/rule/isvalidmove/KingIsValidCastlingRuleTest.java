package org.mark.chess.rulesengine.rule.isvalidmove;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mark.chess.logic.CheckLogic;
import org.mark.chess.logic.MoveLogic;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.mark.chess.model.King;
import org.mark.chess.model.Rook;
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
import static org.mark.chess.enums.PieceType.KING;

@ExtendWith(MockitoExtension.class)
class KingIsValidCastlingRuleTest {
    private static final int LAST_SQUARE_ON_THE_BOARD_ID = 63;

    @InjectMocks
    private KingIsValidCastlingRule kingIsValidCastlingRule;

    @Mock
    private CheckLogic checkLogic;

    @Mock
    private MoveLogic moveLogic;

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
    void testIsValidCastling_CastlingLeft_Valid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(3, 1));

        Coordinates rookCoordinates = new Coordinates(1, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Grid grid = new Grid(List.of(rookField), checkLogic, moveLogic);

//        when(gridLogic.getField(grid, rookCoordinates)).thenReturn(rookField);

        assertTrue(kingIsValidCastlingRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, false)));
        assertTrue(kingIsValidCastlingRule.create());
    }

    @Test
    void testIsValidCastling_CastlingRight_Valid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Grid grid = new Grid(List.of(rookField), checkLogic, moveLogic);

//        when(gridLogic.getField(grid, rookCoordinates)).thenReturn(rookField);

        assertTrue(kingIsValidCastlingRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, false)));
        assertTrue(kingIsValidCastlingRule.create());
    }

    @Test
    void testIsValidCastling_KingHasAlreadyMoved_Invalid() {
        Field from = new Field(new King(WHITE).setHasMovedAtLeastOnce(true)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE)).setCoordinates(rookCoordinates);

        Grid grid = new Grid(List.of(rookField), checkLogic, moveLogic);

        assertFalse(kingIsValidCastlingRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, false)));
    }

    @Test
    void testIsValidCastling_RookHasAlreadyMoved_Invalid() {
        Field from = new Field(new King(WHITE)).setCoordinates(new Coordinates(5, 1));
        Field to = new Field(null).setCoordinates(new Coordinates(7, 1));

        Coordinates rookCoordinates = new Coordinates(8, 1);
        Field rookField = new Field(new Rook(WHITE).setHasMovedAtLeastOnce(true)).setCoordinates(rookCoordinates);

        Grid grid = new Grid(List.of(rookField), checkLogic, moveLogic);

//        when(gridLogic.getField(grid, rookCoordinates)).thenReturn(rookField);

        assertFalse(kingIsValidCastlingRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, false)));
    }

    @Test
    void testRule_WhenInvalidCastling_ThenReturnFalse() {
        Field from = new Field(KING.createPiece(WHITE)).setCode("e1");
        Field to = new Field(null).setCode("d1");

        fields.set(from.getId(), from);

        Grid grid = new Grid(fields, checkLogic, moveLogic);

        assertFalse(kingIsValidCastlingRule.test(new IsValidMoveParameter(grid, from, to, checkLogic, false)));
    }
}