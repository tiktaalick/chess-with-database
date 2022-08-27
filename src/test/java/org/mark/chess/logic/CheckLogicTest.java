package org.mark.chess.logic;

import org.junit.jupiter.api.Test;

class CheckLogicTest {

    @Test
    void isInCheckNow() {
    }

//    @ParameterizedTest
//    @CsvSource(value = {
//            "true;d3;e2;h2;king;e2;false",
//            "false;d3;e2;h2;king;e2;false",
//            "false;d3;e2;h2;king;e1;false",
//            "false;d3;e2;h2;king;f1;true",
//            "false;d3;e2;h2;king;d3;false",
//            "false;d3;f3;e3;pawn;e4;true",
//            "false;d3;f3;e2;pawn;e3;false",
//            "false;d3;f3;e2;pawn;d3;false",
//            "false;c5;c4;h2;king;b5;true",
//            "false;c5;c4;h2;king;c5;false",
//            "false;c5;c4;h2;king;d5;true",
//            "false;c5;c4;h2;king;b4;true",
//            "false;c5;c4;h2;king;c4;false",
//            "false;c5;c4;h2;king;d4;true",
//            "false;c5;c4;h2;king;b3;false",
//            "false;c5;c4;h2;king;c3;true",
//            "false;c5;c4;h2;king;d3;false",}, delimiter = DELIMITER)
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    void testIsMovingIntoCheck_WhenMovingIntoCheck_ThenReturnTrue(boolean isOpponent,
//            String codeOpponentQueen,
//            String codeKing,
//            String codePawn,
//            String nameOfMovingPiece,
//            String toCode,
//            boolean expectedOutcome) {
//        List<Field> fields = IntStream.rangeClosed(0, MAX_SQUARE_ID).mapToObj(id -> {
//            Field field = new Field().setId(id);
//            return field.setButton(new Button(new Board(gameService, WHITE), field));
//        }).collect(Collectors.toList());
//
//        Field queenField = fields
//                .stream()
//                .filter(field -> field.getCode().equals(codeOpponentQueen))
//                .findFirst()
//                .orElse(new Field())
//                .setPiece(new Queen().setColor(BLACK));
//        Field kingFieldBeforeMove = fields
//                .stream()
//                .filter(field -> field.getCode().equals(codeKing))
//                .findFirst()
//                .orElse(new Field())
//                .setPiece(new King().setColor(WHITE));
//        Field pawnField = fields
//                .stream()
//                .filter(field -> field.getCode().equals(codePawn))
//                .findFirst()
//                .orElse(new Field())
//                .setPiece(new Pawn().setColor(WHITE));
//
//        fields.set(queenField.getId(), queenField);
//        fields.set(kingFieldBeforeMove.getId(), kingFieldBeforeMove);
//        fields.set(pawnField.getId(), pawnField);
//
//        Field fromField = fields
//                .stream()
//                .filter(field -> field.getPiece() != null && field.getPiece().getPieceType().getName().equals(nameOfMovingPiece))
//                .findFirst()
//                .orElse(null);
//        Field toField = fields.stream().filter(field -> field.getCode().equals(toCode)).findFirst().orElse(fromField);
//
//        Grid grid = new Grid(fields, gridLogic);
//
//        when(opponentFactory.getLogic(QUEEN)).thenReturn(new QueenLogic(gridLogic, checkLogic));
//
//        assertEquals(expectedOutcome, checkLogic.isMovingIntoCheck(grid, fromField, toField, isOpponent, gridLogic));
//    }
}