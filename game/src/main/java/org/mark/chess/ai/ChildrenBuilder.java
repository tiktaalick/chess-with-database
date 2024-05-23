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
        long start = System.nanoTime();

        Set<Chessboard> children = new HashSet<>();

        forEachValidFromToCombination((from, toList) -> toList.forEach(to -> children.add(this.parent.createOneStepBeyond(from, to))));

        LOGGER.info(() -> this.parent.hashCode() + " Number of children for " + this.activePlayerColor + "=" + children.size());

        GAME_SERVICE.storeDuration(Chessboard.durationMap, "buildChildren()", start, System.nanoTime());

        return new HashSet<>(children);
    }

    public ChildrenBuilder calculateFieldValues(String fromFilter) {
        long start = System.nanoTime();

        this.parent.getFields().forEach(field -> field.setAbsoluteValue(null).setRelativeValue(null));

        createAbsoluteToFieldValues(fromFilter);

        int minValue = getMinValue(this.parent.getAllValidToFields());
        int maxValue = getMaxValue(this.parent.getAllValidToFields());

        LOGGER.info(() -> this.parent.hashCode() + " maxValue=" + maxValue);
        LOGGER.info(() -> this.parent.hashCode() + " minValue=" + minValue);

        createRelativeToFieldValues(fromFilter, minValue, maxValue);

        createRelativeFromFieldValues(fromFilter, minValue, maxValue);

        setBackgroundColors();

        GAME_SERVICE.storeDuration(Chessboard.durationMap, "calculateFieldValues()", start, System.nanoTime());

        return this;
    }

    public ChildrenBuilder init(Chessboard chessboard, Move move, PlayerColor activePlayerColor) {
        long start = System.nanoTime();

        this.activePlayerColor = activePlayerColor;
        this.parent = chessboard;
        this.parent.setAllValidToFields(new ArrayList<>());
        this.parent.setAllValidFromToCombinations(new HashMap<>());

        collectAllValidFromToCombinations(move);

        GAME_SERVICE.storeDuration(Chessboard.durationMap, "init()", start, System.nanoTime());

        return this;
    }

    public ChildrenBuilder resetFromAttributes(Move move, Field from) {
        from.setRelativeValue(null).setAttacking(false).setUnderAttack(false).setValidFrom(false).setValidTo(false);

        LOGGER.info(() -> this.parent.hashCode() + " " + from.getCode() + " has been set to no validFrom and no validTo.");

        resetEnPassant(move, from);

        return this;
    }

    public ChildrenBuilder resetToAttributes(String fromFilter) {
        long start = System.nanoTime();

        this.parent.getFields().forEach(field -> {
            field.setAttacking(false).setUnderAttack(false).setValidFrom(false).setValidTo(false);
            LOGGER.info(() -> this.parent.hashCode() + " " + field.getCode() + " has been set to no validFrom and no validTo.");
        });

        forEachValidFromToCombination((from, toList) -> toList
                .stream()
                .filter(to -> withinSelection(from, fromFilter))
                .forEach(to -> to.setValidTo(true)));

        GAME_SERVICE.storeDuration(Chessboard.durationMap, "resetToAttributes()", start, System.nanoTime());

        return this;
    }

    private static double calculateRelativeFieldValue(int minValue, int maxValue, int fieldValue) {
        long start = System.nanoTime();

        int shiftedMaxValue = max(1, maxValue - minValue);
        int shiftedFieldValue = fieldValue - minValue;
        double fieldValueComparedToAbsoluteMaxValue = ((double) shiftedFieldValue / shiftedMaxValue);

        LOGGER.info(() -> "shiftedMaxValue=" + shiftedMaxValue);
        LOGGER.info(() -> "shiftedFieldValue=" + shiftedFieldValue);
        LOGGER.info(() -> "fieldValueComparedToAbsoluteMaxValue=" + fieldValueComparedToAbsoluteMaxValue);

        GAME_SERVICE.storeDuration(Chessboard.durationMap, "calculateRelativeFieldValue()", start, System.nanoTime());

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

        this.parent.getFields().forEach((Field from) -> this.resetFromAttributes(move, from).createAllValidFromToCombinations(from));

        LOGGER.info(() -> this.parent.hashCode() + " Number of allValidToFields=" + this.parent.getAllValidToFields().size());
        LOGGER.info(() -> this.parent.hashCode() + " Number of allValidFromToCombinations=" + this.parent.getAllValidFromToCombinations().size());

        GAME_SERVICE.storeDuration(Chessboard.durationMap, "collectAllValidFromToCombinations()", start, System.nanoTime());
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
        long start = System.nanoTime();

        forEachValidFromToCombination((from, validToFields) -> validToFields.forEach(to -> {
            to.setAbsoluteValue(this.createAbsoluteToFieldValue(from.setValidFrom(withinSelection(from, fromFilter)), to));

            LOGGER.info(() -> this.parent.hashCode() + " " + from.getCode() + " has been set to validFrom=" + withinSelection(from, fromFilter));
            LOGGER.info(() -> this.parent.hashCode() + " " + from.getCode() + " -> " + to.getCode() + ": to.value=" + to.getAbsoluteValue());
        }));

        GAME_SERVICE.storeDuration(Chessboard.durationMap, "createAbsoluteToFieldValues()", start, System.nanoTime());
    }

    private void createAllValidFromToCombinations(Field from) {
        long start = System.nanoTime();

        List<Field> validToFields = createValidToFields(from, this.activePlayerColor);

        from.setValidFrom(!validToFields.isEmpty());
        LOGGER.info(() -> this.parent.hashCode() + " " + from.getCode() + " has been set to validFrom=" + !validToFields.isEmpty());

        this.parent.getAllValidToFields().addAll(validToFields);

        if (from.isValidFrom()) {
            this.parent.getAllValidFromToCombinations().put(from, validToFields);
        }

        GAME_SERVICE.storeDuration(Chessboard.durationMap, "createAllValidFromToCombinations()", start, System.nanoTime());
    }

    private void createRelativeFromFieldValues(String fromFilter, int minValue, int maxValue) {
        long start = System.nanoTime();

        forEachValidFromToCombination((from, validToFields) -> {
            if (withinSelection(from, fromFilter)) {
                from.setRelativeValue((int) calculateRelativeFieldValue(minValue,
                        maxValue,
                        validToFields.stream().mapToInt(Field::getAbsoluteValue).max().orElse(0)));

                LOGGER.info(() -> this.parent.hashCode() + " " + from.getCode() + ": from.relativeValue=" + from.getRelativeValue());
            }
        });

        GAME_SERVICE.storeDuration(Chessboard.durationMap, "createRelativeFromFieldValues()", start, System.nanoTime());
    }

    private void createRelativeToFieldValues(String fromFilter, int minValue, int maxValue) {
        long start = System.nanoTime();

        forEachValidFromToCombination((from, validToFields) -> validToFields.stream().filter(to -> withinSelection(from, fromFilter)).forEach(to -> {
            to.setRelativeValue((int) calculateRelativeFieldValue(minValue, maxValue, to.getAbsoluteValue()));

            LOGGER.info(() -> this.parent.hashCode() + " " + from.getCode() + " -> " + to.getCode() + ": to.relativeValue=" + to.getRelativeValue());
        }));

        GAME_SERVICE.storeDuration(Chessboard.durationMap, "createRelativeToFieldValues()", start, System.nanoTime());
    }

    private List<Field> createValidToFields(@NotNull Field from, PlayerColor activePlayerColor) {
        long start = System.nanoTime();

        List<Field> validToFields = from.isActivePlayerField(activePlayerColor) ? this.parent
                .getFields()
                .stream()
                .filter(to -> from.getPieceType().isValidMove(new IsValidMoveParameter(this.parent, from, to, false)))
                .toList() : new ArrayList<>();

        GAME_SERVICE.storeDuration(Chessboard.durationMap, "createValidToFields()", start, System.nanoTime());

        return validToFields;
    }

    private void forEachValidFromToCombination(BiConsumer<Field, List<Field>> validFromToCombinationConsumer) {
        this.parent.getAllValidFromToCombinations().forEach(validFromToCombinationConsumer);
    }

    private int minimaxValue(Field from, Field to) {
        return this.activePlayerColor == BLACK
               ? max(from.getAbsoluteValue(), to.getAbsoluteValue())
               : min(from.getAbsoluteValue(), to.getAbsoluteValue());
    }

    private void setBackgroundColors() {
        long start = System.nanoTime();

        this.parent.getFields().forEach(gridField -> gridField.setBackgroundColor(BACKGROUND_COLOR_RULES_ENGINE.process(gridField)));

        GAME_SERVICE.storeDuration(Chessboard.durationMap, "setBackgroundColors()", start, System.nanoTime());
    }
}
