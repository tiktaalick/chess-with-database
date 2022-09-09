package org.mark.chess.logic;

import org.mark.chess.enums.Color;
import org.mark.chess.enums.PieceType;
import org.mark.chess.factory.InitialPieceFactory;
import org.mark.chess.model.Coordinates;
import org.mark.chess.model.Field;
import org.mark.chess.model.Game;
import org.mark.chess.model.Grid;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.awt.GridLayout;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mark.chess.enums.Color.WHITE;

@Service
public class GridLogic {
    static final         int        NUMBER_OF_COLUMNS_AND_ROWS = 8;
    private static final int        MAXIMUM_SQUARE_ID          = 63;
    private final        CheckLogic checkLogic;
    private final        FieldLogic fieldLogic;
    private final        MoveLogic  moveLogic;

    @Lazy
    public GridLogic(FieldLogic fieldLogic, CheckLogic checkLogic, MoveLogic moveLogic) {
        this.fieldLogic = fieldLogic;
        this.checkLogic = checkLogic;
        this.moveLogic = moveLogic;
    }

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

    static GridLayout createGridLayout() {
        return new GridLayout(NUMBER_OF_COLUMNS_AND_ROWS, NUMBER_OF_COLUMNS_AND_ROWS);
    }

    Field getField(Grid grid, Button button) {
        return grid.getFields().stream().filter(field -> button.equals(field.getButton())).findFirst().orElse(null);
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
        return Grid.createGrid(IntStream
                .rangeClosed(0, MAXIMUM_SQUARE_ID)
                .map(id -> (game.getHumanPlayerColor() == WHITE)
                        ? id
                        : (MAXIMUM_SQUARE_ID - id))
                .mapToObj(id -> fieldLogic.addChessPiece(fieldLogic.initializeField(board, id), InitialPieceFactory.getInitialPiece(id)))
                .collect(Collectors.toList()), this, fieldLogic);
    }

    void setKingFieldFlags(Game game, Collection<Field> allValidMoves, Field kingField) {
        boolean isInCheckNow = checkLogic.isInCheckNow(game.getGrid(), kingField, kingField, false);
        kingField
                .setCheckMate(moveLogic.isNotAbleToMove(game, kingField, allValidMoves) && isInCheckNow)
                .setStaleMate(moveLogic.isNotAbleToMove(game, kingField, allValidMoves) && !isInCheckNow);
    }
}
