package org.mark.chess.ai;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.mark.chess.board.Chessboard;
import org.mark.chess.board.Field;
import org.mark.chess.board.backgroundcolor.BackgroundColorRulesEngine;
import org.mark.chess.move.Move;
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

    private static final Logger LOGGER = Logger.getLogger(ChildrenBuilder.class.getName());

    @Setter
    private PlayerColor activePlayerColor;
    private Chessboard  parent;

    public Set<Chessboard> buildChildren() {
        Set<Chessboard> children = new HashSet<>();

        forEachValidFromToCombination((from, toList) -> toList.forEach(to -> children.add(this.parent.createOneStepBeyond(from, to))));

        LOGGER.info(() -> "Number of children for " + this.activePlayerColor + "=" + children.size());

        return new HashSet<>(children);
    }

    public ChildrenBuilder calculateFieldValues(String fromFilter) {
        this.parent.getFields().forEach(field -> field.setValue(null).setRelativeValue(null));

        forEachValidFromToCombination((from, validToFields) -> validToFields.forEach(to -> {
            this.createAbsoluteFieldValues(from.setValidFrom(withinSelection(from, fromFilter)), to);
            LOGGER.info(() -> from.getCode() + " -> " + to.getCode() + ": to.value=" + to.getValue());
        }));

        int minValue = getMinValue(this.parent.getAllValidToFields());
        int maxValue = getMaxValue(this.parent.getAllValidToFields());

        LOGGER.info(() -> "minValue=" + minValue);
        LOGGER.info(() -> "maxValue=" + maxValue);

        forEachValidFromToCombination((from, validToFields) -> validToFields.stream().filter(to -> withinSelection(from, fromFilter)).forEach(to -> {
            to.setRelativeValue(createRelativeFieldValueTo(to, minValue, maxValue));
            LOGGER.info(() -> from.getCode() + " -> " + to.getCode() + ": to.relativeValue=" + to.getRelativeValue());
        }));

        forEachValidFromToCombination((from, validToFields) -> {
            if (withinSelection(from, fromFilter)) {
                from.setRelativeValue(createRelativeFieldValueFrom(validToFields, minValue, maxValue));
                LOGGER.info(() -> from.getCode() + ": from.relativeValue=" + from.getRelativeValue());
            }
        });

        return this;
    }

    /**
     * Collects all valid from/to combinations.
     *
     * @param move The current move.
     * @return this.
     */
    public ChildrenBuilder collectAllValidFromToCombinations(Move move) {
        this.parent.getFields().forEach((Field from) -> this.resetFromAttributes(move, from).createAllValidFromToCombinations(from));

        LOGGER.info(() -> "Number of allValidToFields=" + this.parent.getAllValidToFields().size());
        LOGGER.info(() -> "Number of allValidFromToCombinations=" + this.parent.getAllValidFromToCombinations().size());

        return this;
    }

    public ChildrenBuilder init(Chessboard chessboard, PlayerColor activePlayerColor) {
        this.activePlayerColor = activePlayerColor;
        this.parent = chessboard;
        this.parent.setAllValidToFields(new ArrayList<>());
        this.parent.setAllValidFromToCombinations(new HashMap<>());

        return this;
    }

    public ChildrenBuilder resetFromAttributes(Move move, Field from) {
        from.setRelativeValue(null).setAttacking(false).setUnderAttack(false).setValidFrom(false).setValidTo(false);

        resetEnPassant(move, from);

        return this;
    }

    public ChildrenBuilder resetToAttributes(String fromFilter) {
        this.parent.getFields().forEach(field -> field.setAttacking(false).setUnderAttack(false).setValidFrom(false).setValidTo(false));

        forEachValidFromToCombination((from, toList) -> toList
                .stream()
                .filter(to -> withinSelection(from, fromFilter))
                .forEach(to -> to.setValidTo(true)));

        return this;
    }

    public ChildrenBuilder setBackgroundColors() {
        this.parent.getFields().forEach(gridField -> gridField.setBackgroundColor(BACKGROUND_COLOR_RULES_ENGINE.process(gridField)));

        return this;
    }

    private static double calculateRelativeValue(int minValue, int maxValue, int fieldValue) {
        int shiftedMaxValue = max(1, maxValue - minValue);
        int shiftedFieldValue = fieldValue - minValue;
        double fieldValueComparedToAbsoluteMaxValue = ((double) shiftedFieldValue / shiftedMaxValue);

        LOGGER.info(() -> "shiftedMaxValue=" + shiftedMaxValue);
        LOGGER.info(() -> "shiftedFieldValue=" + shiftedFieldValue);
        LOGGER.info(() -> "fieldValueComparedToAbsoluteMaxValue=" + fieldValueComparedToAbsoluteMaxValue);

        return (int) (fieldValueComparedToAbsoluteMaxValue * (MAXIMUM_COLOR_VALUE - MINIMUM_COLOR_VALUE) + MINIMUM_COLOR_VALUE);
    }

    private static int createRelativeFieldValueFrom(List<Field> validToFields, int minValue, int maxValue) {
        return (int) calculateRelativeValue(minValue, maxValue, validToFields.stream().mapToInt(Field::getValue).max().orElse(0));
    }

    private static int createRelativeFieldValueTo(Field to, int minValue, int maxValue) {
        return (int) calculateRelativeValue(minValue, maxValue, to.getValue());
    }

    private static int getMaxValue(Collection<Field> validToFields) {
        return validToFields == null ? 0 : validToFields.stream().filter(field -> field.getValue() != null).mapToInt(Field::getValue).max().orElse(0);
    }

    private static int getMinValue(Collection<Field> validToFields) {
        return validToFields == null ? 0 : validToFields.stream().filter(field -> field.getValue() != null).mapToInt(Field::getValue).min().orElse(0);
    }

    private static void resetEnPassant(Move move, Field from) {
        if (!move.isDuringAMove(from) && from.getPieceType() != null && from.getPieceType().getName().equals(PAWN)) {
            ((Pawn) from.getPieceType()).setMayBeCapturedEnPassant(false);
        }
    }

    private static boolean withinSelection(Field from, String fromFilter) {
        return Optional.ofNullable(fromFilter).orElse(from.getCode()).equals(from.getCode());
    }

    private void createAbsoluteFieldValues(Field from, Field to) {
        if (from != null && from.getPieceType() != null) {
            var chessboardAfterMovement = this.parent.createOneStepBeyond(from, to);
            to.setValue(CHESSBOARD_VALUE_RULES_ENGINE
                    .process(new ChessboardValueParameter(chessboardAfterMovement, this.activePlayerColor))
                    .getTotalValue());
            from.setValue(from.getValue() == null ? to.getValue() : minimaxValue(from, to));
        }
    }

    private void createAllValidFromToCombinations(Field from) {
        List<Field> validToFields = this.parent.createValidToFields(from, this.activePlayerColor);

        from.setValidFrom(!validToFields.isEmpty());

        this.parent.getAllValidToFields().addAll(validToFields);

        if (from.isValidFrom()) {
            this.parent.getAllValidFromToCombinations().put(from, validToFields);
        }
    }

    private void forEachValidFromToCombination(BiConsumer<Field, List<Field>> validFromToCombinationConsumer) {
        this.parent.getAllValidFromToCombinations().forEach(validFromToCombinationConsumer);
    }

    private int minimaxValue(Field from, Field to) {
        return this.activePlayerColor == BLACK ? max(from.getValue(), to.getValue()) : min(from.getValue(), to.getValue());
    }
}
