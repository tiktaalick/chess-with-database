package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.InitialPieceFactory;
import org.mark.chess.factory.PieceLogicFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Grid;
import org.mark.chess.swing.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.swing.JButton;
import java.awt.GridLayout;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.factory.BackgroundColorFactory.MAX_COLOR_VALUE;
import static org.mark.chess.factory.BackgroundColorFactory.MIN_COLOR_VALUE;
import static org.mark.chess.logic.PieceLogic.backgroundColorFactory;

public class GridLogic {
    public static final int NUMBER_OF_COLUMNS_AND_ROWS = 8;

    @Autowired
    private FieldLogic fieldLogic;

    @Autowired
    private InitialPieceFactory initialPieceFactory;

    @Autowired
    @Lazy
    private KingLogic kingLogic;

    @Autowired
    private PieceLogicFactory pieceLogicFactory;

    public int calculateGridValue(Grid grid, Color activePlayerColor) {
        return grid
                .getFields()
                .stream()
                .filter(field -> field.getPiece() != null)
                .mapToInt(field -> field.getPiece().getColor() == activePlayerColor
                        ? field.getPiece().getPieceType().getValue()
                        : -field.getPiece().getPieceType().getValue())
                .sum();
    }

    public GridLayout createGridLayout() {
        return new GridLayout(NUMBER_OF_COLUMNS_AND_ROWS, NUMBER_OF_COLUMNS_AND_ROWS);
    }

    public Field getField(Grid grid, Coordinates coordinates) {
        return grid
                .getFields()
                .stream()
                .filter(field -> field.getCoordinates().getX() == coordinates.getX())
                .filter(field -> field.getCoordinates().getY() == coordinates.getY())
                .findFirst()
                .orElse(null);
    }

    public Field getField(Grid grid, JButton button) {
        return grid.getFields().stream().filter(field -> field.getButton() == button).findFirst().orElse(null);
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

    public Grid initializeGrid(Game game, Board board) {
        return new Grid(IntStream
                .rangeClosed(0, 63)
                .map(id -> game.getHumanPlayerColor() == WHITE
                        ? id
                        : 63 - id)
                .mapToObj(id -> fieldLogic.addChessPiece(fieldLogic.initializeField(board, id), initialPieceFactory.getInitialPiece(id)))
                .collect(Collectors.toList()));
    }

    public void setKingFieldColors(Game game, List<Field> allValidMoves) {
        game.getGrid().getFields().stream().filter(field -> field.getPiece() != null).forEach(field -> {
            if (field.getPiece().getPieceType() == PieceType.KING) {
                boolean isInCheckNow = kingLogic.isInCheckNow(game.getGrid(), field, field, pieceLogicFactory, false);
                field
                        .setCheckMate(isNotAbleToMove(game, field, allValidMoves) && isInCheckNow)
                        .setStaleMate(isNotAbleToMove(game, field, allValidMoves) && !isInCheckNow);
            }

            game.setInProgress(game.isInProgress()
                    ? !field.isCheckMate() && !field.isStaleMate()
                    : game.isInProgress());

            if (!game.isInProgress()) {
                field.getButton().setBackground(backgroundColorFactory.getBackgroundColor(field));
            }
        });
    }

    public void setValidMoveColors(Grid grid, Field from, List<Field> validMoves, List<Field> allValidMoves) {
        System.out.println();
        grid.getFields().forEach(field -> field.setValue(null).setRelativeValue(null));
        allValidMoves.forEach(to -> createAbsoluteFieldValues(grid, from, to));
        createRelativeFieldValues(validMoves, allValidMoves, from);
    }

    private void createAbsoluteFieldValues(Grid grid, Field from, Field to) {
        if (from != null && from.getPiece() != null) {
            Grid gridAfterMovement = new Grid(grid, from, to);

            to.setValue(gridAfterMovement.getGridValue());
            from.setValue(from.getValue() == null
                    ? to.getValue()
                    : Math.max(from.getValue(), to.getValue()));
        }
    }

    private void createRelativeFieldValues(List<Field> validMoves, List<Field> allValidMoves, Field from) {
        int minValue = getMinValue(allValidMoves);
        int maxValue = getMaxValue(allValidMoves);
        validMoves.forEach(gridField -> {
            double relativeValue = maxValue - minValue <= 0
                    ? MAX_COLOR_VALUE
                    : (((double) gridField.getValue() - minValue) / (maxValue - minValue)) * (MAX_COLOR_VALUE - MIN_COLOR_VALUE) + MIN_COLOR_VALUE;
            gridField.setRelativeValue((int) relativeValue);

            from.setRelativeValue(from.getRelativeValue() == null
                    ? gridField.getRelativeValue()
                    : Math.max(from.getRelativeValue(), gridField.getRelativeValue()));

            System.out.println(from.getCode() +
                               ": " +
                               from.getValue() +
                               " " +
                               from.getRelativeValue() +
                               "\t-->\t" +
                               gridField.getCode() +
                               ": " +
                               gridField.getValue() +
                               " " +
                               gridField.getRelativeValue() +
                               "\t(" +
                               minValue +
                               " - " +
                               maxValue +
                               ")");
            gridField.getButton().setBackground(backgroundColorFactory.getBackgroundColor(gridField));
        });

        from.getButton().setBackground(backgroundColorFactory.getBackgroundColor(from));
    }

    private int getMaxValue(List<Field> validMoves) {
        return validMoves == null
                ? 0
                : validMoves.stream().mapToInt(Field::getValue).max().orElse(0);
    }

    private int getMinValue(List<Field> validMoves) {
        return validMoves == null
                ? 0
                : validMoves.stream().mapToInt(Field::getValue).min().orElse(0);
    }

    private boolean isNotAbleToMove(Game game, Field field, List<Field> allValidMoves) {
        return game.getCurrentPlayerColor() == field.getPiece().getColor() && game.isInProgress() && allValidMoves.isEmpty();
    }
}
