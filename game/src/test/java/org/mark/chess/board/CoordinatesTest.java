package org.mark.chess.board;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mark.chess.board.Coordinates;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CoordinatesTest {
    private static final char DELIMITER = ';';

    @ParameterizedTest
    @CsvSource(value = {
            "1;8;a8", "2;7;b7", "3;6;c6", "4;5;d5", "5;4;e4", "6;3;f3", "7;2;g2", "8;1;h1"}, delimiter = DELIMITER)
    void testCreateCode_WhenCoordinatesProvided_ThenReturnCorrespondingCode(int x, int y, String code) {
        assertEquals(code, Coordinates.createCode(new Coordinates(x, y)));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0;a8", "9;b7", "18;c6", "27;d5", "36;e4", "45;f3", "54;g2", "63;h1"}, delimiter = DELIMITER)
    void testCreateCode_WhenIdProvided_ThenReturnCorrespondingCode(int id, String code) {
        assertEquals(code, Coordinates.createCode(id));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1;8;a8", "2;7;b7", "3;6;c6", "4;5;d5", "5;4;e4", "6;3;f3", "7;2;g2", "8;1;h1"}, delimiter = DELIMITER)
    void testCreateCode_WhenXAndYProvided_ThenReturnCorrespondingCode(int x, int y, String code) {
        assertEquals(code, Coordinates.createCode(x, y));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1;1;a1", "2;2;b2", "3;3;c3", "4;4;d4", "5;5;e5", "6;6;f6", "7;7;g7", "8;8;h8"}, delimiter = DELIMITER)
    void testCreateCoordinates_WhenCodeProvided_ThenReturnCorrespondingCoordinates(int x, int y, String code) {
        assertEquals(x, Coordinates.create(code).getX());
        assertEquals(y, Coordinates.create(code).getY());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1;8;0", "2;7;9", "3;6;18", "4;5;27", "5;4;36", "6;3;45", "7;2;54", "8;1;63"}, delimiter = DELIMITER)
    void testCreateCoordinates_WhenIdProvided_ThenReturnCorrespondingCoordinates(int x, int y, int id) {
        assertEquals(x, Coordinates.create(id).getX());
        assertEquals(y, Coordinates.create(id).getY());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "a8;0", "b7;9", "c6;18", "d5;27", "e4;36", "f3;45", "g2;54", "h1;63"}, delimiter = DELIMITER)
    void testCreateId_WhenCodeProvided_ThenReturnCorrespondingId(String code, int id) {
        assertEquals(id, Coordinates.createId(code));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0;0;0", "1;1;9", "2;2;18", "3;3;27", "4;4;36", "5;5;45", "6;6;54", "7;7;63"}, delimiter = DELIMITER)
    void testCreateId_WhenCoordinatesProvided_ThenReturnCorrespondingId(int x, int y, int id) {
        assertEquals(id, Coordinates.createId(new Coordinates(x, y)));
    }
}