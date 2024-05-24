package org.mark.chess.ai;

import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Field;
import org.mark.chess.board.backgroundcolor.BackgroundColorRulesEngine;
import org.mark.chess.game.GameService;
import org.mark.chess.move.Move;
import org.mark.chess.piece.general.isvalidmove.IsValidMoveParameter;
import org.mark.chess.piece.pawn.Pawn;
import org.mark.chess.player.PlayerColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.mark.chess.board.Chessboard.MAXIMUM_COLOR_VALUE;
import static org.mark.chess.board.Chessboard.MINIMUM_COLOR_VALUE;
import static org.mark.chess.piece.general.PieceType.PAWN;
import static org.mark.chess.player.PlayerColor.BLACK;

@Accessors(chain = true)
public class ChildrenBuilder {

    private static final BackgroundColorRulesEngine BACKGROUND_COLOR_RULES_ENGINE = new BackgroundColorRulesEngine();
    private static final ChessboardValueRulesEngine CHESSBOARD_VALUE_RULES_ENGINE = new ChessboardValueRulesEngine();
    private static final GameService                GAME_SERVICE                  = new GameService();
    private static final Logger                     LOGGER                        = Logger.getLogger(ChildrenBuilder.class.getName());

    private PlayerColor activePlayerColor;
    private Chessboard  parent;

    public Set<Chessboard> buildChildren() {
        Set<Chessboard> children = new HashSet<>();

        forEachValidFromToCombination((from, toList) -> toList.forEach(to -> children.add(this.parent.createOneStepBeyond(from, to))));

        log("Number of children for " + this.activePlayerColor + "=" + children.size());

        return new HashSet<>(children);
    }

    public ChildrenBuilder calculateFieldValues(String fromFilter) {
        this.parent.getFields().forEach(field -> field.setAbsoluteValue(null).setRelativeValue(null));

        createAbsoluteToFieldValues(fromFilter);

        int minValue = getMinValue(this.parent.getAllValidToFields());
        int maxValue = getMaxValue(this.parent.getAllValidToFields());

        log("maxValue=" + maxValue);
        log("minValue=" + minValue);

        createRelativeToFieldValues(fromFilter, minValue, maxValue);

        createRelativeFromFieldValues(fromFilter, minValue, maxValue);

        setBackgroundColors();

        return this;
    }

    public ChildrenBuilder init(Chessboard chessboard, Move move, PlayerColor activePlayerColor) {
        this.activePlayerColor = activePlayerColor;
        this.parent = chessboard;
        this.parent.setAllValidToFields(new ArrayList<>());
        this.parent.setAllValidFromToCombinations(new HashMap<>());

        collectAllValidFromToCombinations(move);

        return this;
    }

    public ChildrenBuilder resetFromAttributes(Move move, Field from) {
        from.setRelativeValue(null).setAttacking(false).setUnderAttack(false).setValidFrom(false).setValidTo(false);

        resetEnPassant(move, from);

        return this;
    }

    public ChildrenBuilder resetToAttributes(String fromFilter) {
        long start = System.nanoTime();

        this.parent.getFields().forEach(field -> field.setAttacking(false).setUnderAttack(false).setValidFrom(false).setValidTo(false));

        forEachValidFromToCombination((from, toList) -> toList
                .stream()
                .filter(to -> withinSelection(from, fromFilter))
                .forEach(to -> to.setValidTo(true)));

        GAME_SERVICE.storeDuration("childrenBuilder.resetToAttributes()", start, System.nanoTime());

        return this;
    }

    private static double calculateRelativeFieldValue(int minValue, int maxValue, int fieldValue) {
        int shiftedMaxValue = max(1, maxValue - minValue);
        int shiftedFieldValue = fieldValue - minValue;
        double fieldValueComparedToAbsoluteMaxValue = ((double) shiftedFieldValue / shiftedMaxValue);

        LOGGER.info(() -> "shiftedMaxValue=" + shiftedMaxValue);
        LOGGER.info(() -> "shiftedFieldValue=" + shiftedFieldValue);
        LOGGER.info(() -> "fieldValueComparedToAbsoluteMaxValue=" + fieldValueComparedToAbsoluteMaxValue);

        return (int) (fieldValueComparedToAbsoluteMaxValue * (MAXIMUM_COLOR_VALUE - MINIMUM_COLOR_VALUE) + MINIMUM_COLOR_VALUE);
    }

    private static int getMaxValue(Collection<Field> validToFields) {
        return validToFields.stream().filter(field -> field.getAbsoluteValue() != null).mapToInt(Field::getAbsoluteValue).max().orElse(0);
    }

    private static int getMinValue(Collection<Field> validToFields) {
        return validToFields.stream().filter(field -> field.getAbsoluteValue() != null).mapToInt(Field::getAbsoluteValue).min().orElse(0);
    }

    private static void resetEnPassant(Move move, Field from) {
        if (!move.isDuringAMove(from) && from.getPieceType() != null && from.getPieceType().getName().equals(PAWN)) {
            ((Pawn) from.getPieceType()).setMayBeCapturedEnPassant(false);
        }
    }

    private static boolean withinSelection(Field from, String fromFilter) {
        return Optional.ofNullable(fromFilter).orElse(from.getCode()).equals(from.getCode());
    }

    private void collectAllValidFromToCombinations(Move move) {
        long start = System.nanoTime();

        this.parent.getFields().forEach(from -> this.resetFromAttributes(move, from).createAllValidFromToCombinations(from));

        log("Number of allValidToFields=" + this.parent.getAllValidToFields().size());
        log("Number of allValidFromToCombinations=" + this.parent.getAllValidFromToCombinations().size());

        GAME_SERVICE.storeDuration("childrenBuilder.collectAllValidFromToCombinations()", start, System.nanoTime());
    }

    private int createAbsoluteToFieldValue(Field from, Field to) {
        if (from != null && from.getPieceType() != null) {
            var chessboardAfterMovement = this.parent.createOneStepBeyond(from, to);

            return CHESSBOARD_VALUE_RULES_ENGINE
                    .process(new ChessboardValueParameter(chessboardAfterMovement, this.activePlayerColor))
                    .getTotalValue();
        }

        return 0;
    }

    private void createAbsoluteToFieldValues(String fromFilter) {
        forEachValidFromToCombination((from, validToFields) -> validToFields.forEach(to -> {
            to.setAbsoluteValue(this.createAbsoluteToFieldValue(from.setValidFrom(withinSelection(from, fromFilter)), to));

            log(from.getCode() + " -> " + to.getCode() + ": to.value=" + to.getAbsoluteValue());
        }));
    }

    private void createAllValidFromToCombinations(Field from) {
        long start = System.nanoTime();

        if (!from.isActivePlayerField(activePlayerColor)) {
            return;
        }

        List<Field> validToFields = createValidToFields(from, this.activePlayerColor);

        from.setValidFrom(!validToFields.isEmpty());

        this.parent.getAllValidToFields().addAll(validToFields);

        if (from.isValidFrom()) {
            this.parent.getAllValidFromToCombinations().put(from, validToFields);
        }

        GAME_SERVICE.storeDuration("childrenBuilder.createAllValidFromToCombinations()", start, System.nanoTime());
    }

    private void createRelativeFromFieldValues(String fromFilter, int minValue, int maxValue) {
        forEachValidFromToCombination((from, validToFields) -> {
            if (withinSelection(from, fromFilter)) {
                from.setRelativeValue(maxValue - minValue == 0
                                      ? 0
                                      : (int) calculateRelativeFieldValue(minValue,
                                              maxValue,
                                              validToFields.stream().mapToInt(Field::getAbsoluteValue).max().orElse(0)));

                log(from.getCode() + ": from.relativeValue=" + from.getRelativeValue());
            }
        });
    }

    private void createRelativeToFieldValues(String fromFilter, int minValue, int maxValue) {
        forEachValidFromToCombination((from, validToFields) -> validToFields.stream().filter(to -> withinSelection(from, fromFilter)).forEach(to -> {
            to.setRelativeValue(to.getAbsoluteValue() == 0 ? 0 : (int) calculateRelativeFieldValue(minValue, maxValue, to.getAbsoluteValue()));

            log(from.getCode() + " -> " + to.getCode() + ": to.relativeValue=" + to.getRelativeValue());
        }));
    }

    private List<Field> createValidToFields(@NotNull Field from, PlayerColor activePlayerColor) {
        long start = System.nanoTime();

        List<Field> validToFields = from.isActivePlayerField(activePlayerColor) ? this.parent
                .getFields()
                .stream()
                .filter(to -> !to.isActivePlayerField(activePlayerColor))
                .filter(to -> from.getPieceType().isValidMove(new IsValidMoveParameter(this.parent, from, to, false)))
                .toList() : new ArrayList<>();

        GAME_SERVICE.storeDuration("childrenBuilder.createValidToFields()", start, System.nanoTime());

        return validToFields;
    }

    private void forEachValidFromToCombination(BiConsumer<Field, List<Field>> validFromToCombinationConsumer) {
        this.parent.getAllValidFromToCombinations().forEach(validFromToCombinationConsumer);
    }

    private void log(String message) {
        LOGGER.info(() -> this.parent.hashCode() + " " + message);
    }

    private int minimaxValue(Field from, Field to) {
        return this.activePlayerColor == BLACK
               ? max(from.getAbsoluteValue(), to.getAbsoluteValue())
               : min(from.getAbsoluteValue(), to.getAbsoluteValue());
    }

    private void setBackgroundColors() {
        this.parent.getFields().forEach(gridField -> gridField.setBackgroundColor(BACKGROUND_COLOR_RULES_ENGINE.process(gridField)));
    }
}
