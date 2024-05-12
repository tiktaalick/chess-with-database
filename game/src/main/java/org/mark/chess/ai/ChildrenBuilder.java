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
import java.util.List;

import static org.mark.chess.board.Chessboard.MAXIMUM_COLOR_VALUE;
import static org.mark.chess.board.Chessboard.MINIMUM_COLOR_VALUE;
import static org.mark.chess.piece.general.PieceType.PAWN;
import static org.mark.chess.player.PlayerColor.BLACK;

@Accessors(chain = true)
public class ChildrenBuilder {

    private static final BackgroundColorRulesEngine BACKGROUND_COLOR_RULES_ENGINE = new BackgroundColorRulesEngine();
    private static final ChessboardValueRulesEngine CHESSBOARD_VALUE_RULES_ENGINE = new ChessboardValueRulesEngine();

    @Setter
    private Chessboard  parent;
    @Setter
    private PlayerColor activePlayerColor;

    private List<Field>      allValidToFields = new ArrayList<>();
    private List<Chessboard> children         = new ArrayList<>();

    public List<Chessboard> buildChildren() {
        this.parent
                .getAllValidFromToCombinations()
                .forEach((from, toList) -> toList.forEach(to -> this.children.add(this.parent.createOneStepBeyond(from, to))));

        return this.children;
    }

    public ChildrenBuilder calculateFieldValues() {
        this.parent.getAllValidFromToCombinations().forEach((from, validToFields) -> calculateFieldValues(from));

        return this;
    }

    /**
     * Collects all valid from/to combinations.
     *
     * @param move The current move.
     * @return this.
     */
    public ChildrenBuilder collectAllValidFromToCombinations(Move move) {
        this.parent.getFields().forEach((Field from) -> this.resetFromAttributes(move, from).setValidToFields(from));

        return this;
    }

    public ChildrenBuilder resetFromAttributes(Move move, Field from) {
        from.setAttacking(false).setUnderAttack(false).setValidFrom(false).setValidTo(false);

        if (!move.isDuringAMove(from) && from.getPieceType() != null && from.getPieceType().getName().equals(PAWN)) {
            ((Pawn) from.getPieceType()).setMayBeCapturedEnPassant(false);
        }
        return this;
    }

    public ChildrenBuilder setValidToFields(Field from) {
        List<Field> validToFields = this.parent.createValidToFields(from, this.activePlayerColor);

        from.setValidTo(!validToFields.isEmpty()).setValidFrom(from.hasValidTo());

        this.allValidToFields.addAll(validToFields);

        if (from.isValidFrom()) {
            this.parent.getAllValidFromToCombinations().put(from, validToFields);
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
        this.parent.getFields().forEach(field -> field.setValue(null).setRelativeValue(null));

        this.allValidToFields.forEach(to -> createAbsoluteFieldValues(from, to));

        this.createRelativeFieldValues(from);
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

    private void createRelativeFieldValues(@NotNull Field from) {
        int minValue = getMinValue(this.allValidToFields);
        int maxValue = getMaxValue(this.allValidToFields);
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
        return this.activePlayerColor == BLACK ? Math.max(from.getValue(), to.getValue()) : Math.min(from.getValue(), to.getValue());
    }
}
