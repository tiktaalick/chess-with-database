package org.mark.chess.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.ApplicationFactory;
import org.mark.chess.factory.BackgroundColorFactory;
import org.mark.chess.rulesengine.parameter.IsValidMoveParameter;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;

import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mark.chess.enums.Color.BLACK;
import static org.mark.chess.enums.Color.WHITE;
import static org.mark.chess.enums.PieceType.PAWN;
import static org.mark.chess.factory.BackgroundColorFactory.MAX_COLOR_VALUE;
import static org.mark.chess.factory.BackgroundColorFactory.MIN_COLOR_VALUE;

@Data
@Accessors(chain = true)
public class Game {
    private static final int LEFT_CLICK        = 1;
    private static final int MAXIMUM_SQUARE_ID = 63;
    private static final int RIGHT_CLICK       = 3;

    private boolean      inProgress;
    private List<Player> players            = Arrays.asList(new Human().setColor(WHITE), new Computer().setColor(BLACK));
    private Color        humanPlayerColor;
    private Color        currentPlayerColor = WHITE;
    private Grid         grid;
    private Move         move               = new Move(new Field(null));

    public Game(boolean inProgress, Color humanPlayerColor, Grid grid) {
        this.inProgress = inProgress;
        this.humanPlayerColor = humanPlayerColor;
        this.grid = grid;
    }

    public static Game create(Board board, Color humanPlayerColor) {
        return new Game(true, humanPlayerColor, Grid.create(board, humanPlayerColor));
    }

    public List<Field> getValidMoves(Field from) {
        return from.isActivePlayerField(this)
                ? this
                .getGrid()
                .getFields()
                .stream()
                .filter(to -> from.getPiece().getPieceType().isValidMove(new IsValidMoveParameter(grid, from, to, false)))
                .collect(Collectors.toList())
                : new ArrayList<>();
    }

    public void handleButtonClick(Window board, int buttonClick, Button button) {
        var fieldClick = this.getGrid().getField(button);

        if (!this.isInProgress()) {
            board.dispose();
            ApplicationFactory.getInstance().startApplication(this.getHumanPlayerColor().getOpposite());
        } else if (buttonClick == LEFT_CLICK && fieldClick.isValidMove() && move.isFrom(this, fieldClick)) {
            this.move.setFrom(fieldClick).enableValidMoves(this, fieldClick);
        } else if (buttonClick == LEFT_CLICK && fieldClick.isValidMove() && !move.isFrom(this, fieldClick)) {
            this.move
                    .setTo(this.getGrid(), fieldClick)
                    .setPieceTypeSpecificFields(this, move.getFrom(), fieldClick)
                    .moveRookWhenCastling(this, move.getFrom(), fieldClick)
                    .changeTurn(this)
                    .resetField(move.getFrom());
            this.setKingFieldColors(this.resetValidMoves());
        } else if (buttonClick == RIGHT_CLICK) {
            this.setKingFieldColors(this.resetValidMoves());
        }
    }

    public List<Field> resetValidMoves() {
        Map<Field, List<Field>> allValidFromsAndValidMoves = new HashMap<>();
        List<Field> allValidMoves = new ArrayList<>();

        this.getGrid().getFields().forEach((Field from) -> {
            from.setAttacking(false).setUnderAttack(false).setValidFrom(false);

            setValidMoves(allValidFromsAndValidMoves, allValidMoves, from);

            if (!move.duringAMove(from) && from.getPiece() != null && from.getPiece().getPieceType() == PAWN) {
                ((Pawn) from.getPiece()).setMayBeCapturedEnPassant(false);
            }
        });

        allValidFromsAndValidMoves.forEach((from, validMoves) -> setValidMoveColors(getGrid(), from, validMoves, allValidMoves));

        return allValidMoves;
    }

    public void setGameProgress(Field kingField) {
        this.setInProgress(this.isInProgress()
                ? (!kingField.isCheckMate() && !kingField.isStaleMate())
                : this.isInProgress());
    }

    public void setKingFieldColors(Collection<Field> allValidMoves) {
        this.getGrid().getFields().stream().filter(field -> field.getPiece() != null).forEach((Field field) -> {
            if (field.getPiece().getPieceType() == PieceType.KING) {
                Grid.setKingFieldFlags(this, allValidMoves, field);
                this.setGameProgress(field);
            }

            if (!this.isInProgress()) {
                field.getButton().setBackground(BackgroundColorFactory.getBackgroundColor(field));
            }
        });
    }

    public void setValidMoveColors(Grid grid, Field from, Collection<Field> validMoves, Collection<Field> allValidMoves) {
        grid.getFields().forEach(field -> field.setValue(null).setRelativeValue(null));
        allValidMoves.forEach(to -> createAbsoluteFieldValues(grid, from, to));
        createRelativeFieldValues(validMoves, allValidMoves, from);
    }

    void createAbsoluteFieldValues(Grid grid, Field from, Field to) {
        if (from != null && from.getPiece() != null) {
            var gridAfterMovement = Grid.createAfterMovement(grid, from, to);

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

    private void setValidMoves(Map<Field, List<Field>> allValidFromsAndValidMoves, List<Field> allValidMoves, Field from) {
        List<Field> validMoves = getValidMoves(from);
        from.setValidMove(!validMoves.isEmpty());
        from.setValidFrom(from.isValidMove());
        allValidMoves.addAll(validMoves);

        if (from.isValidFrom()) {
            allValidFromsAndValidMoves.put(from, validMoves);
        }
    }
}
