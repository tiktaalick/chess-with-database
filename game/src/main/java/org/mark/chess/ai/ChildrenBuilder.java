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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mark.chess.board.Chessboard.MAXIMUM_COLOR_VALUE;
import static org.mark.chess.board.Chessboard.MINIMUM_COLOR_VALUE;
import static org.mark.chess.piece.general.PieceType.PAWN;
import static org.mark.chess.player.PlayerColor.BLACK;

@Accessors(chain = true)
public class ChildrenBuilder {

    private static final BackgroundColorRulesEngine BACKGROUND_COLOR_RULES_ENGINE = new BackgroundColorRulesEngine();
    private static final ChessboardValueRulesEngine CHESSBOARD_VALUE_RULES_ENGINE = new ChessboardValueRulesEngine();
    private static final Logger                     LOGGER                        = Logger.getLogger(ChildrenBuilder.class.getName());
    private final        List<Chessboard>           children                      = new ArrayList<>();

    @Setter
    private PlayerColor activePlayerColor;
    private Chessboard  parent;
    private List<Field> allValidToFields = new ArrayList<>();

    public List<Chessboard> buildChildren() {
        this.parent
                .getAllValidFromToCombinations()
                .forEach((from, toList) -> toList.forEach(to -> this.children.add(this.parent.createOneStepBeyond(from, to))));

        LOGGER.log(Level.INFO, () -> "Number of children for " + this.activePlayerColor + "=" + this.children.size());

        return new ArrayList<>(this.children);
    }

    public void calculateFieldValues() {
        LOGGER.log(Level.INFO, () -> "Calculating field values...");

        this.parent.getAllValidFromToCombinations().forEach((from, validToFields) -> {
            from.setValidFrom(true);
            validToFields.forEach(to -> to.setValidTo(true));
            calculateFieldValues(from);
        });
    }

    /**
     * Collects all valid from/to combinations.
     *
     * @param move The current move.
     * @return this.
     */
    public ChildrenBuilder collectAllValidFromToCombinations(Move move) {
        LOGGER.log(Level.INFO, () -> "Collecting all valid from/to combinations...");

        this.parent.getFields().forEach((Field from) -> this.resetFromAttributes(move, from).setValidToFields(from));

        return this;
    }

    public ChildrenBuilder init(Chessboard chessboard, PlayerColor activePlayerColor) {
        this.parent = chessboard;
        this.activePlayerColor = activePlayerColor;
        this.parent.setChildren(new ArrayList<>());
        this.allValidToFields = new ArrayList<>();
        this.parent.setAllValidFromToCombinations(new HashMap<>());

        return this;
    }

    public ChildrenBuilder resetFromAttributes(Move move, Field from) {
        LOGGER.log(Level.INFO, () -> "Resetting from attributes...");

        from.setAttacking(false).setUnderAttack(false).setValidFrom(false).setValidTo(false);

        if (!move.isDuringAMove(from) && from.getPieceType() != null && from.getPieceType().getName().equals(PAWN)) {
            ((Pawn) from.getPieceType()).setMayBeCapturedEnPassant(false);
        }
        return this;
    }

    public ChildrenBuilder setValidToFields(Field from) {
        List<Field> validToFields = this.parent.createValidToFields(from, this.activePlayerColor);

        LOGGER.log(Level.INFO, () -> "Number of validToFields=" + validToFields.size());

        from.setValidTo(!validToFields.isEmpty()).setValidFrom(from.hasValidTo());

        this.allValidToFields.addAll(validToFields);

        LOGGER.log(Level.INFO, () -> "Number of allValidToFields=" + this.allValidToFields.size());

        if (from.isValidFrom()) {
            this.parent.getAllValidFromToCombinations().put(from, validToFields);
            LOGGER.log(Level.INFO, () -> "Number of allValidFromToCombinations=" + this.parent.getAllValidFromToCombinations().size());
        }

        return this;
    }

    private static double calculateRelativeValue(int minValue, int maxValue, Field gridField) {
        return (((double) getCurrentFieldValueComparedToMinimumValue(gridField, minValue)) /
                getMaximumFieldValueComparedToMinimumValue(minValue, maxValue)) * (MAXIMUM_COLOR_VALUE - MINIMUM_COLOR_VALUE) + MINIMUM_COLOR_VALUE;
    }

    private static int getCurrentFieldValueComparedToMinimumValue(@NotNull Field gridField, int minValue) {
        return gridField.getValue() - minValue;
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

    private void calculateFieldValues(Field from) {
        LOGGER.log(Level.INFO, () -> "Calculating field values...");

        this.parent.getFields().forEach(field -> field.setValue(null).setRelativeValue(null));

        this.allValidToFields.forEach(to -> createAbsoluteFieldValues(from, to));

        this.createRelativeFieldValues(from);
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

    private void createRelativeFieldValues(@NotNull Field from) {
        LOGGER.log(Level.INFO, () -> "Creating relative field values...");

        int minValue = getMinValue(this.allValidToFields);
        int maxValue = getMaxValue(this.allValidToFields);

        LOGGER.log(Level.INFO, () -> "minValue=" + minValue);
        LOGGER.log(Level.INFO, () -> "maxValue=" + maxValue);

        this.allValidToFields.forEach((Field gridField) -> {
            double relativeValue = maxValue - minValue <= 0 ? MAXIMUM_COLOR_VALUE : calculateRelativeValue(minValue, maxValue, gridField);

            gridField.setRelativeValue((int) relativeValue);

            from.setRelativeValue(from.getRelativeValue() == null
                                  ? gridField.getRelativeValue()
                                  : Math.max(from.getRelativeValue(), gridField.getRelativeValue()));

            gridField.setBackgroundColor(BACKGROUND_COLOR_RULES_ENGINE.process(gridField));
        });

        from.setBackgroundColor(BACKGROUND_COLOR_RULES_ENGINE.process(from));
    }

    private int minimaxValue(Field from, Field to) {
        LOGGER.log(Level.INFO, () -> "activePlayerColor=" + this.activePlayerColor);

        return this.activePlayerColor == BLACK ? Math.max(from.getValue(), to.getValue()) : Math.min(from.getValue(), to.getValue());
    }
}
