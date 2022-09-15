package org.mark.chess.logic;

import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.model.Field;
import org.mark.chess.model.Grid;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static org.mark.chess.factory.BackgroundColorFactory.MAX_COLOR_VALUE;
import static org.mark.chess.factory.BackgroundColorFactory.MIN_COLOR_VALUE;

@Service
public class GridValueLogic {
    private final GridLogic gridLogic;

    public GridValueLogic(GridLogic gridLogic) {
        this.gridLogic = gridLogic;
    }

    void createAbsoluteFieldValues(Grid grid, Field from, Field to) {
        if (from != null && from.getPiece() != null) {
            var gridAfterMovement = Grid.createGrid(grid, from, to, gridLogic);

            to.setValue(gridAfterMovement.getGridValue());
            from.setValue(from.getValue() == null
                    ? to.getValue()
                    : Math.max(from.getValue(), to.getValue()));
        }
    }

    void createRelativeFieldValues(Collection<Field> validMoves, Collection<Field> allValidMoves, Field from) {
        int minValue = getMinValue(allValidMoves);
        int maxValue = getMaxValue(allValidMoves);
        validMoves.forEach((Field gridField) -> {
            double relativeValue = maxValue - minValue <= 0
                    ? MAX_COLOR_VALUE
                    : calculateRelativeValue(minValue, maxValue, gridField);

            gridField.setRelativeValue((int) relativeValue);

            from.setRelativeValue(from.getRelativeValue() == null
                    ? gridField.getRelativeValue()
                    : Math.max(from.getRelativeValue(), gridField.getRelativeValue()));

            gridField.getButton().setBackground(BackgroundColorFactory.getBackgroundColor(gridField));
        });

        from.getButton().setBackground(BackgroundColorFactory.getBackgroundColor(from));
    }

    private static double calculateRelativeValue(int minValue, int maxValue, Field gridField) {
        return (((double) getCurrentFieldValueComparedToMinimumValue(gridField, minValue)) /
                getMaximumFieldValueComparedToMinimumValue(minValue, maxValue)) * (MAX_COLOR_VALUE - MIN_COLOR_VALUE) + MIN_COLOR_VALUE;
    }

    private static int getCurrentFieldValueComparedToMinimumValue(Field gridField, int minValue) {
        return gridField.getValue() - minValue;
    }

    private static int getMaxValue(Collection<Field> validMoves) {
        return validMoves == null
                ? 0
                : validMoves.stream().mapToInt(Field::getValue).max().orElse(0);
    }

    private static int getMaximumFieldValueComparedToMinimumValue(int minValue, int maxValue) {
        return (maxValue - minValue);
    }

    private static int getMinValue(Collection<Field> validMoves) {
        return validMoves == null
                ? 0
                : validMoves.stream().mapToInt(Field::getValue).min().orElse(0);
    }
}
