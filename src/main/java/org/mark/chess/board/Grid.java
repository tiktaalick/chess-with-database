package org.mark.chess.board;

import lombok.Data;
import lombok.experimental.Accessors;
import org.mark.chess.game.Game;
import org.mark.chess.piece.InitialPieceRepository;
import org.mark.chess.player.PlayerColor;
import org.mark.chess.swing.Board;
import org.mark.chess.swing.Button;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mark.chess.piece.PieceType.KING;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

@Data
@Accessors(chain = true)
public final class Grid {

    public static final  int NUMBER_OF_COLUMNS_AND_ROWS = 8;
    private static final int MAXIMUM_SQUARE_ID          = 63;

    private List<Field> fields;
    private Field       kingField;
    private Field       opponentKingField;
    private int         gridValue;

    private Grid(List<Field> fields) {
        this.fields = fields;
        this.kingField = getKingField(this, WHITE);
        this.opponentKingField = getKingField(this, BLACK);
        this.gridValue = calculateGridValue(this, WHITE);
    }

    private Grid(Grid gridBeforeTheMove, Field from, Field to) {
        this.fields = gridBeforeTheMove
                .getFields()
                .stream()
                .filter(field -> !Arrays.asList(from.getCode(), to.getCode()).contains(field.getCode()))
                .collect(Collectors.toList());

        List<Field> movementList = gridBeforeTheMove
                .getFields()
                .stream()
                .filter(field -> Arrays.asList(from.getCode(), to.getCode()).contains(field.getCode()))
                .map(field -> Objects.equals(field.getCode(), from.getCode())
                        ? new Field(null).setCoordinates(from.getCoordinates())
                        : new Field(from.getPieceType()).setCoordinates(to.getCoordinates()))
                .collect(Collectors.toList());

        this.fields.addAll(movementList);

        this.kingField = getKingField(this, from.getPieceType().getColor());
        this.opponentKingField = getKingField(this, from.getPieceType().getColor().getOpposite());
        this.gridValue = calculateGridValue(this, from.getPieceType().getColor());
    }

    public static Grid create(Board board, PlayerColor humanPlayerColor) {
        return new Grid(IntStream
                .rangeClosed(0, MAXIMUM_SQUARE_ID)
                .map(id -> (humanPlayerColor == WHITE)
                        ? id
                        : (MAXIMUM_SQUARE_ID - id))
                .mapToObj(id -> new Field(null).initialize(board, id).addChessPiece(InitialPieceRepository.getInitialPiece(id)))
                .collect(Collectors.toList()));
    }

    public static Grid createAfterMovement(Grid gridBeforeTheMove, Field from, Field to) {
        return new Grid(gridBeforeTheMove, from, to);
    }

    public static Grid createEmpty(Board board, PlayerColor humanPlayerColor) {
        return new Grid(IntStream
                .rangeClosed(0, MAXIMUM_SQUARE_ID)
                .map(id -> (humanPlayerColor == WHITE)
                        ? id
                        : (MAXIMUM_SQUARE_ID - id))
                .mapToObj(id -> new Field(null).initialize(board, id))
                .collect(Collectors.toList()));
    }

    public static GridLayout createGridLayout() {
        return new GridLayout(NUMBER_OF_COLUMNS_AND_ROWS, NUMBER_OF_COLUMNS_AND_ROWS);
    }

    public static void setKingFieldFlags(Game game, Collection<Field> allValidMoves, Field kingField) {
        boolean isInCheckNow = kingField.isInCheckNow(game.getGrid(), false);

        kingField
                .setCheckMate(kingField.isNotAbleToMove(game, allValidMoves) && isInCheckNow)
                .setStaleMate(kingField.isNotAbleToMove(game, allValidMoves) && !isInCheckNow);
    }

    public int calculateGridValue(Grid grid, PlayerColor activePlayerColor) {
        return grid
                .getFields()
                .stream()
                .filter(field -> field.getPieceType() != null)
                .mapToInt(field -> field.getPieceType().getColor() == activePlayerColor
                        ? field.getPieceType().getValue()
                        : -field.getPieceType().getValue())
                .sum();
    }

    public Field getField(Coordinates coordinates) {
        return this
                .getFields()
                .stream()
                .filter(field -> field.getCoordinates().getX() == coordinates.getX())
                .filter(field -> field.getCoordinates().getY() == coordinates.getY())
                .findAny()
                .orElse(null);
    }

    public Field getField(String code) {
        return this.getFields().stream().filter(field -> field.getCode() == code).findAny().orElse(null);
    }

    public Field getField(Button button) {
        return this.getFields().stream().filter(field -> button.equals(field.getButton())).findAny().orElse(null);
    }

    public Field getKingField(Grid grid, PlayerColor color) {
        return grid
                .getFields()
                .stream()
                .filter(field -> field.getPieceType() != null)
                .filter(field -> field.getPieceType().getColor() == color)
                .filter(field -> field.getPieceType().getName().equals(KING))
                .findAny()
                .orElse(null);
    }
}
