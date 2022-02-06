package org.mark.chess.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.enums.Color;
import org.mark.chess.factory.InitialPieceFactory;
import org.mark.chess.model.Bishop;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Piece;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mark.chess.logic.GridLogic.NUMBER_OF_COLUMNS_AND_ROWS;
import static org.mark.chess.swing.Button.FIELD_WIDTH;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FieldLogicTest {
    @InjectMocks
    private FieldLogic fieldLogic;

    @Mock
    private Board board;

    @Mock
    private ButtonLogic buttonLogic;

    @Mock
    private InitialPieceFactory initialPieceFactory;

    @Test
    void testInitializeField_WhenDark_ThenReturnDarkField() {
        Game game = new Game();

        Field field = fieldLogic.initializeField(board, 34);

        assertEquals("c4", field.getCode());
        assertEquals(3 - 1 + (NUMBER_OF_COLUMNS_AND_ROWS - 4) * NUMBER_OF_COLUMNS_AND_ROWS, field.getId());
        assertEquals(3, field.getCoordinates().getX());
        assertEquals(4, field.getCoordinates().getY());
        assertEquals(3 * FIELD_WIDTH, field.getButton().getX());
        assertEquals(4 * FIELD_WIDTH, field.getButton().getY());
        assertEquals(Color.DARK.getAwtColor(), field.getButton().getBackground());
        assertFalse(field.getButton().isEnabled());
    }

    @Test
    void testInitializeField_WhenLight_ThenReturnLightField() {
        Game game = new Game();

        Field field = fieldLogic.initializeField(board, 26);

        assertEquals("c5", field.getCode());
        assertEquals(3 - 1 + (NUMBER_OF_COLUMNS_AND_ROWS - 5) * NUMBER_OF_COLUMNS_AND_ROWS, field.getId());
        assertEquals(3, field.getCoordinates().getX());
        assertEquals(5, field.getCoordinates().getY());
        assertEquals(3 * FIELD_WIDTH, field.getButton().getX());
        assertEquals(5 * FIELD_WIDTH, field.getButton().getY());
        assertEquals(Color.LIGHT.getAwtColor(), field.getButton().getBackground());
        assertFalse(field.getButton().isEnabled());
    }

    @Test
    void testAddChessPiece() {
        Field field1 = new Field().setCode("b8");
        Field field2 = new Field().setCode("c8");
        Field field3 = new Field().setCode("d8");
        List<Field> grid = new ArrayList<>(Arrays.asList(new Field().setId(0).setCoordinates(new Coordinates(0, 0)), field1, field2, field3));

        Game game = new Game().setGrid(grid);
        Piece bishop = new Bishop();

        when(buttonLogic.initializeButton(game, field2)).thenReturn(new Button(board, field2));

        fieldLogic.addChessPiece(game, field2, bishop.setColor(Color.BLACK));

        assertEquals(Color.BLACK, game.getGrid().get(2).getPiece().getColor());
        assertEquals(bishop, game.getGrid().get(2).getPiece());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "a8;0", "b7;9", "c6;18", "d5;27", "e4;36", "f3;45", "g2;54", "h1;63"}, delimiter = ';')
    void testCreateId_WhenCodeProvided_ThenReturnCorrespondingId(String code, int id) {
        assertEquals(id, fieldLogic.createId(code));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0;0;0", "1;1;9", "2;2;18", "3;3;27", "4;4;36", "5;5;45", "6;6;54", "7;7;63"}, delimiter = ';')
    void testCreateId_WhenCoordinatesProvided_ThenReturnCorrespondingId(int x, int y, int id) {
        assertEquals(id, fieldLogic.createId(new Coordinates(x, y)));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0;a8", "9;b7", "18;c6", "27;d5", "36;e4", "45;f3", "54;g2", "63;h1"}, delimiter = ';')
    void testCreateCode_WhenIdProvided_ThenReturnCorrespondingCode(int id, String code) {
        assertEquals(code, fieldLogic.createCode(id));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1;8;a8", "2;7;b7", "3;6;c6", "4;5;d5", "5;4;e4", "6;3;f3", "7;2;g2", "8;1;h1"}, delimiter = ';')
    void testCreateCode_WhenCoordinatesProvided_ThenReturnCorrespondingCode(int x, int y, String code) {
        assertEquals(code, fieldLogic.createCode(new Coordinates(x, y)));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1;8;a8", "2;7;b7", "3;6;c6", "4;5;d5", "5;4;e4", "6;3;f3", "7;2;g2", "8;1;h1"}, delimiter = ';')
    void testCreateCode_WhenXAndYProvided_ThenReturnCorrespondingCode(int x, int y, String code) {
        assertEquals(code, fieldLogic.createCode(x, y));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1;1;a1", "2;2;b2", "3;3;c3", "4;4;d4", "5;5;e5", "6;6;f6", "7;7;g7", "8;8;h8"}, delimiter = ';')
    void testCreateCoordinates_WhenCodeProvided_ThenReturnCorrespondingCoordinates(int x, int y, String code) {
        assertEquals(x, fieldLogic.createCoordinates(code).getX());
        assertEquals(y, fieldLogic.createCoordinates(code).getY());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1;8;0", "2;7;9", "3;6;18", "4;5;27", "5;4;36", "6;3;45", "7;2;54", "8;1;63"}, delimiter = ';')
    void testCreateCoordinates_WhenIdProvided_ThenReturnCorrespondingCoordinates(int x, int y, int id) {
        assertEquals(x, fieldLogic.createCoordinates(id).getX());
        assertEquals(y, fieldLogic.createCoordinates(id).getY());
    }
}