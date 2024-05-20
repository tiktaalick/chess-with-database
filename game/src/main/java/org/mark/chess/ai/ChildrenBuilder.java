package org.mark.chess.ai;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
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
    private static final Logger                     LOGGER                        = Logger.getLogger(ChildrenBuilder.class.getName());

    @Setter
    private PlayerColor activePlayerColor;
    private Chessboard  parent;

    public Set<Chessboard> buildChildren() {
        Set<Chessboard> children = new HashSet<>();

        this.parent
                .getAllValidFromToCombinations()
                .forEach((from, toList) -> toList.forEach(to -> children.add(this.parent.createOneStepBeyond(from, to))));

        LOGGER.log(Level.INFO, () -> "Number of children for " + this.activePlayerColor + "=" + children.size());

        return new HashSet<>(children);
    }

    public ChildrenBuilder calculateFieldValues() {
        LOGGER.log(Level.INFO, () -> "Calculating field values...");

        AtomicInteger minValue = new AtomicInteger(0);
        AtomicInteger maxValue = new AtomicInteger(0);
        new AtomicInteger(getMaxValue(this.parent.getAllValidToFields()));

        this.parent.getFields().forEach(field -> field.setValue(null).setRelativeValue(null));

        this.parent.getAllValidFromToCombinations().forEach((from, validToFields) -> {
            from.setValidFrom(true);
            validToFields.forEach(to -> {
                this.createAbsoluteFieldValues(from, to);
                minValue.set(min(minValue.get(), getMinValue(validToFields)));
                maxValue.set(max(maxValue.get(), getMaxValue(validToFields)));
                this.createRelativeFieldValuesTo(from, to, minValue, maxValue);
            });
        });

        this.parent
                .getAllValidFromToCombinations()
                .forEach((from, validToFields) -> this.createRelativeFieldValuesFrom(from, validToFields, minValue, maxValue));

        return this;
    }

    /**
     * Collects all valid from/to combinations.
     *
     * @param move The current move.
     * @return this.
     */
    public ChildrenBuilder collectAllValidFromToCombinations(Move move) {
        LOGGER.log(Level.INFO, () -> "Collecting all valid from/to combinations...");

        this.parent.getFields().forEach((Field from) -> this.resetFromAttributes(move, from).createAllValidFromToCombinations(from));

        return this;
    }

    public ChildrenBuilder createAllValidFromToCombinations(Field from) {
        List<Field> validToFields = this.parent.createValidToFields(from, this.activePlayerColor);

        LOGGER.log(Level.INFO, () -> "Number of validToFields=" + validToFields.size());

        from.setValidFrom(!validToFields.isEmpty());

        this.parent.getAllValidToFields().addAll(validToFields);

        LOGGER.log(Level.INFO, () -> "Number of allValidToFields=" + this.parent.getAllValidToFields().size());

        if (from.isValidFrom()) {
            this.parent.getAllValidFromToCombinations().put(from, validToFields);
            LOGGER.log(Level.INFO, () -> "Number of allValidFromToCombinations=" + this.parent.getAllValidFromToCombinations().size());
        }

        return this;
    }

    public ChildrenBuilder init(Chessboard chessboard, PlayerColor activePlayerColor) {
        this.activePlayerColor = activePlayerColor;
        this.parent = chessboard;
        this.parent.setAllValidToFields(new ArrayList<>());
        this.parent.setAllValidFromToCombinations(new HashMap<>());

        return this;
    }

    public ChildrenBuilder resetEnPassant(Move move, Field from) {
        LOGGER.log(Level.INFO, () -> "Resetting en passant...");

        if (!move.isDuringAMove(from) && from.getPieceType() != null && from.getPieceType().getName().equals(PAWN)) {
            ((Pawn) from.getPieceType()).setMayBeCapturedEnPassant(false);
        }

        return this;
    }

    public ChildrenBuilder resetFromAttributes(Move move) {
        LOGGER.log(Level.INFO, () -> "Resetting all from attributes...");

        this.parent.getFields().forEach((Field from) -> this.resetFromAttributes(move, from));

        return this;
    }

    public ChildrenBuilder resetFromAttributes(Move move, Field from) {
        LOGGER.log(Level.INFO, () -> "Resetting from attributes...");

        from.setRelativeValue(null).setAttacking(false).setUnderAttack(false).setValidFrom(false).setValidTo(false);

        resetEnPassant(move, from);

        return this;
    }

    public ChildrenBuilder resetToAttributes() {
        LOGGER.log(Level.INFO, () -> "Resetting from attributes...");

        this.parent.getFields().forEach(field -> field.setAttacking(false).setUnderAttack(false).setValidFrom(false).setValidTo(false));

        this.parent.getAllValidFromToCombinations().forEach((from, toList) -> toList.forEach(to -> to.setValidTo(true)));

        return this;
    }

    public ChildrenBuilder setBackgroundColors() {
        LOGGER.log(Level.INFO, () -> "Calculating field values...");

        this.parent.getFields().forEach(gridField -> gridField.setBackgroundColor(BACKGROUND_COLOR_RULES_ENGINE.process(gridField)));

        return this;
    }

    private static double calculateRelativeValue(int minValue, int maxValue, int fieldValue) {
        return (((double) getCurrentFieldValueComparedToMinimumValue(fieldValue, minValue)) /
                getMaximumFieldValueComparedToMinimumValue(minValue, maxValue)) * (MAXIMUM_COLOR_VALUE - MINIMUM_COLOR_VALUE) + MINIMUM_COLOR_VALUE;
    }

    private static int getCurrentFieldValueComparedToMinimumValue(int fieldValue, int minValue) {
        return fieldValue - minValue;
    }

    private static int getMaxValue(Collection<Field> validToFields) {
        return validToFields == null ? 0 : validToFields.stream().filter(field -> field.getValue() != null).mapToInt(Field::getValue).max().orElse(0);
    }

    private static int getMaximumFieldValueComparedToMinimumValue(int minValue, int maxValue) {
        return (maxValue - minValue);
    }

    private static int getMinValue(Collection<Field> validToFields) {
        return validToFields == null ? 0 : validToFields.stream().filter(field -> field.getValue() != null).mapToInt(Field::getValue).min().orElse(0);
    }

    private static double relativeValue(AtomicInteger minValue, AtomicInteger maxValue, int fieldValue) {
        return Optional.ofNullable(fieldValue).orElse(0) == 0
               ? MINIMUM_COLOR_VALUE
               : calculateRelativeValue(minValue.get(), maxValue.get(), fieldValue);
    }

    private void createAbsoluteFieldValues(Field from, Field to) {
        LOGGER.log(Level.INFO, () -> "Creating absolute field values...");

        if (from != null && from.getPieceType() != null) {
            var chessboardAfterMovement = this.parent.createOneStepBeyond(from, to);
            to.setValue(CHESSBOARD_VALUE_RULES_ENGINE
                    .process(new ChessboardValueParameter(chessboardAfterMovement, this.activePlayerColor))
                    .getTotalValue());
            from.setValue(from.getValue() == null ? to.getValue() : minimaxValue(from, to));

            LOGGER.log(Level.INFO, () -> "FromValue=" + from.getValue());
            LOGGER.log(Level.INFO, () -> "ToValue=" + to.getValue());
        }
    }

    private void createRelativeFieldValuesFrom(@NotNull Field from, List<Field> validToFields, AtomicInteger minValue, AtomicInteger maxValue) {
        LOGGER.log(Level.INFO, () -> "Creating relative field values (from)...");

        validToFields.forEach((Field to) -> from.setRelativeValue(max(Optional.ofNullable(from.getRelativeValue()).orElse(0), to.getValue())));

        from.setRelativeValue((int) relativeValue(minValue, maxValue, from.getRelativeValue()));

        LOGGER.log(Level.INFO, () -> "from.getRelativeValue()=" + from.getRelativeValue());
        LOGGER.log(Level.INFO, () -> "minRelativeValue=" + minValue.get());
        LOGGER.log(Level.INFO, () -> "maxRelativeValue=" + maxValue.get());
    }

    private void createRelativeFieldValuesTo(@NotNull Field from, Field to, AtomicInteger minValue, AtomicInteger maxValue) {
        LOGGER.log(Level.INFO, () -> "Creating relative field values (to)...");
        LOGGER.log(Level.INFO, () -> "minValue=" + minValue);
        LOGGER.log(Level.INFO, () -> "maxValue=" + maxValue);

        to.setRelativeValue((int) relativeValue(minValue, maxValue, to.getValue()));

        LOGGER.log(Level.INFO, () -> "from " + from.getCode() + " -> to " + to.getCode());
        LOGGER.log(Level.INFO, () -> "to.getValue()=" + to.getValue());
        LOGGER.log(Level.INFO, () -> "to.getRelativeValue()=" + to.getRelativeValue());
    }

    private int minimaxValue(Field from, Field to) {
        LOGGER.log(Level.INFO, () -> "activePlayerColor=" + this.activePlayerColor);

        return this.activePlayerColor == BLACK ? max(from.getValue(), to.getValue()) : min(from.getValue(), to.getValue());
    }
}
