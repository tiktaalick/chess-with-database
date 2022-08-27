package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.factory.InitialPieceFactory;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Grid;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.JButton;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.factory.BackgroundColorFactory.MAX_COLOR_VALUE;
import static org.mark.chess.factory.BackgroundColorFactory.MIN_COLOR_VALUE;

@Component
public class GridLogic {
    static final         int NUMBER_OF_COLUMNS_AND_ROWS = 8;
    private static final int MAXIMUM_SQUARE_ID          = 63;

    private FieldLogic        fieldLogic;
    private CheckLogic        checkLogic;
    private PieceLogicFactory pieceLogicFactory;

    @Autowired
    public GridLogic(FieldLogic fieldLogic, CheckLogic checkLogic, PieceLogicFactory pieceLogicFactory) {
        this.fieldLogic = fieldLogic;
        this.checkLogic = checkLogic;
        this.pieceLogicFactory = pieceLogicFactory;
    }

    public static int calculateGridValue(Grid grid, Color activePlayerColor) {
        return grid
                .getFields()
                .stream()
                .filter(field -> field.getPiece() != null)
                .mapToInt(field -> field.getPiece().getColor() == activePlayerColor
                        ? field.getPiece().getPieceType().getValue()
                        : -field.getPiece().getPieceType().getValue())
                .sum();
    }

    static GridLayout createGridLayout() {
        return new GridLayout(NUMBER_OF_COLUMNS_AND_ROWS, NUMBER_OF_COLUMNS_AND_ROWS);
    }

    private static void createRelativeFieldValues(Iterable<Field> validMoves, Collection<Field> allValidMoves, Field from) {
        int minValue = getMinValue(allValidMoves);
        int maxValue = getMaxValue(allValidMoves);
        validMoves.forEach(gridField -> {
            double relativeValue = maxValue - minValue <= 0
                    ? MAX_COLOR_VALUE
                    : (((double) getCurrentFieldValueComparedToMinimumValue(gridField, minValue)) /
                       getMaximumFieldValueComparedToMinimumValue(minValue, maxValue)) * (MAX_COLOR_VALUE - MIN_COLOR_VALUE) + MIN_COLOR_VALUE;

            gridField.setRelativeValue((int) relativeValue);

            from.setRelativeValue(from.getRelativeValue() == null
                    ? gridField.getRelativeValue()
                    : Math.max(from.getRelativeValue(), gridField.getRelativeValue()));

            gridField.getButton().setBackground(BackgroundColorFactory.getBackgroundColor(gridField));
        });

        from.getButton().setBackground(BackgroundColorFactory.getBackgroundColor(from));
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

    private static boolean isNotAbleToMove(Game game, Field field, Collection<Field> allValidMoves) {
        return game.getCurrentPlayerColor() == field.getPiece().getColor() && game.isInProgress() && allValidMoves.isEmpty();
    }

    public Field getKingField(Grid grid, Color color) {
        return grid
                .getFields()
                .stream()
                .filter(field -> field.getPiece() != null)
                .filter(field -> field.getPiece().getColor() == color)
                .filter(field -> field.getPiece().getPieceType() == PieceType.KING)
                .findFirst()
                .orElse(null);
    }

    Field getField(Grid grid, JButton button) {
        return grid.getFields().stream().filter(field -> field.getButton() == button).findFirst().orElse(null);
    }

    Field getField(Grid grid, Coordinates coordinates) {
        return grid
                .getFields()
                .stream()
                .filter(field -> field.getCoordinates().getX() == coordinates.getX())
                .filter(field -> field.getCoordinates().getY() == coordinates.getY())
                .findFirst()
                .orElse(null);
    }

    Grid initializeGrid(Game game, Board board) {
        return new Grid(IntStream
                .rangeClosed(0, MAXIMUM_SQUARE_ID)
                .map(id -> game.getHumanPlayerColor() == WHITE
                        ? id
                        : MAXIMUM_SQUARE_ID - id)
                .mapToObj(id -> fieldLogic.addChessPiece(fieldLogic.initializeField(board, id), InitialPieceFactory.getInitialPiece(id)))
                .collect(Collectors.toList()), this);
    }

    void setKingFieldColors(Game game, Collection<Field> allValidMoves) {
        game.getGrid().getFields().stream().filter(field -> field.getPiece() != null).forEach(field -> {
            if (field.getPiece().getPieceType() == PieceType.KING) {
                boolean isInCheckNow = checkLogic.isInCheckNow(game.getGrid(), field, field, false);
                field
                        .setCheckMate(isNotAbleToMove(game, field, allValidMoves) && isInCheckNow)
                        .setStaleMate(isNotAbleToMove(game, field, allValidMoves) && !isInCheckNow);
            }

            game.setInProgress(game.isInProgress()
                    ? !field.isCheckMate() && !field.isStaleMate()
                    : game.isInProgress());

            if (!game.isInProgress()) {
                field.getButton().setBackground(BackgroundColorFactory.getBackgroundColor(field));
            }
        });
    }

    void setValidMoveColors(Grid grid, Field from, Iterable<Field> validMoves, Collection<Field> allValidMoves) {
        grid.getFields().forEach(field -> field.setValue(null).setRelativeValue(null));
        allValidMoves.forEach(to -> createAbsoluteFieldValues(grid, from, to));
        createRelativeFieldValues(validMoves, allValidMoves, from);
    }

    private void createAbsoluteFieldValues(Grid grid, Field from, Field to) {
        if (from != null && from.getPiece() != null) {
            Grid gridAfterMovement = new Grid(grid, from, to, this);

            to.setValue(gridAfterMovement.getGridValue());
            from.setValue(from.getValue() == null
                    ? to.getValue()
                    : Math.max(from.getValue(), to.getValue()));
        }
    }
}
