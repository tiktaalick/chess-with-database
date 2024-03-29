package org.mark.chess.board;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.mark.chess.game.Game;
import org.mark.chess.piece.InitialPieceRepository;
import org.mark.chess.player.PlayerColor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mark.chess.piece.PieceType.KING;
import static org.mark.chess.player.PlayerColor.BLACK;
import static org.mark.chess.player.PlayerColor.WHITE;

/**
 * Contains methods related to the backend representation of a chessboard.
 */
@Getter
@Setter
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

    private Grid(@NotNull Grid gridBeforeTheMove, @NotNull Field from, Field to) {
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

    /**
     * Creates a chessboard with chess pieces in their initial positions.
     *
     * @return A chessboard with chess pieces in their initial positions.
     */
    public static @NotNull Grid create() {
        return new Grid(IntStream
                .rangeClosed(0, MAXIMUM_SQUARE_ID)
                .mapToObj(id -> new Field(null).setId(id).setPieceType(InitialPieceRepository.getInitialPiece(id)))
                .collect(Collectors.toList()));
    }

    /**
     * Creates a chessboard with chess pieces in their future positions, based on their current positions and the current move.
     *
     * @param gridBeforeTheMove The chessboard with the chess pieces in their positions
     * @param from              The field from which a piece is moving.
     * @param to                The field to which a piece is moving.
     * @return A chessboard with chess pieces in their future positions.
     */
    public static @NotNull Grid createAfterMovement(Grid gridBeforeTheMove, Field from, Field to) {
        return new Grid(gridBeforeTheMove, from, to);
    }

    /**
     * Creates a chessboard without chess pieces.
     *
     * @return A chessboard without chess pieces.
     */
    public static @NotNull Grid createEmpty() {
        return new Grid(IntStream.rangeClosed(0, MAXIMUM_SQUARE_ID).mapToObj(id -> new Field(null).setId(id)).collect(Collectors.toList()));
    }

    /**
     * Sets the checkmate or stalemate flag on a field that contains a king, if applicable.
     *
     * @param game          The game.
     * @param allValidMoves All the valid moves.
     * @param kingField     The field that contains a king.
     */
    public static void setKingFieldFlags(@NotNull Game game, Collection<Field> allValidMoves, @NotNull Field kingField) {
        boolean isInCheckNow = kingField.isInCheckNow(game.getGrid(), false);

        kingField
                .setCheckMate(kingField.isNotAbleToMove(game, allValidMoves) && isInCheckNow)
                .setStaleMate(kingField.isNotAbleToMove(game, allValidMoves) && !isInCheckNow);
    }

    /**
     * Calculates the value of the current positions of the chess pieces on the chessboard for the active player.
     *
     * @param grid              The backend representation of the chessboard.
     * @param activePlayerColor The player who is currently the active player.
     * @return The value of the current positions of the chess pieces on the chessboard.
     */
    public int calculateGridValue(@NotNull Grid grid, PlayerColor activePlayerColor) {
        return grid
                .getFields()
                .stream()
                .filter(field -> field.getPieceType() != null)
                .mapToInt(field -> field.getPieceType().getColor() == activePlayerColor
                        ? field.getPieceType().getValue()
                        : -field.getPieceType().getValue())
                .sum();
    }

    /**
     * Retrieves a field based on its {@link Coordinates}.
     *
     * @param coordinates The {@link Coordinates} of the field.
     * @return The field.
     */
    public Field getField(Coordinates coordinates) {
        return this
                .getFields()
                .stream()
                .filter(field -> field.getCoordinates().getX() == coordinates.getX())
                .filter(field -> field.getCoordinates().getY() == coordinates.getY())
                .findAny()
                .orElse(null);
    }

    /**
     * Retrieves a field based on the code of the field.
     *
     * @param code The code of the field.
     * @return The field.
     */
    public Field getField(String code) {
        return this.getFields().stream().filter(field -> field.getCode() == code).findAny().orElse(null);
    }

    /**
     * Retrieves a field that contains a king in the given color.
     *
     * @param grid  The backend representation of a chessboard.
     * @param color The color of the king.
     * @return The field that contains a king in the given color.
     */
    public Field getKingField(@NotNull Grid grid, PlayerColor color) {
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
